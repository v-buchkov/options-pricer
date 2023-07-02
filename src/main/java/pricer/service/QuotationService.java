package pricer.service;

import org.springframework.stereotype.Service;
import pricer.modeling.ModelingParams;
import pricer.modeling.Option;
import pricer.modeling.american.AmericanCall;
import pricer.modeling.american.AmericanPut;
import pricer.modeling.european.EuropeanCall;
import pricer.modeling.european.EuropeanPut;
import pricer.modeling.market_maker.GlostenMilgromModel;
import pricer.modeling.ModelingUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QuotationService {
    private final MarketDataService marketDataService;

    // keep base asset fixed in MVP (will be passed as a parameter from the client in future versions)
    QuotationEnums.BASE_ASSET baseAsset = QuotationEnums.BASE_ASSET.USDRUB;

    public final double currentSpot;

    private final double riskFreeRate;
    private final double initialSpot;
    private final double volatility;

    private final double averageQty;

    public QuotationService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;

        var quotationDate = LocalDateTime.now();
        var backwardLookingDate = quotationDate.plusDays(-ModelingParams.volatilitySignificantDays);

        this.riskFreeRate = marketDataService.getCurrentRiskFreeRate(baseAsset);
        this.currentSpot = marketDataService.getCurrentSpot(baseAsset);
        this.volatility = marketDataService.getHistoricalVolatility(baseAsset, backwardLookingDate.toString(),
                quotationDate.toString());

        this.averageQty = marketDataService.getAverageQty(baseAsset, backwardLookingDate.toString(),
                quotationDate.toString());

        this.initialSpot = currentSpot;
    }

    public double[] getQuote(UUID pricingId, QuotationEnums.INSTRUMENT instrument, Integer qty, SIDE side,
                           Integer minutesAvailable, double term, double strike) {
        var option = switch (instrument) {
            case EUROPEAN_CALL -> new EuropeanCall(pricingId, term, volatility, currentSpot, initialSpot,
                    riskFreeRate, strike);
            case EUROPEAN_PUT -> new EuropeanPut(pricingId, term, volatility, currentSpot, initialSpot,
                    riskFreeRate, strike);
            case AMERICAN_CALL -> new AmericanCall(pricingId, term, volatility, currentSpot, initialSpot,
                    riskFreeRate, strike);
            case AMERICAN_PUT -> new AmericanPut(pricingId, term, volatility, currentSpot, initialSpot,
                    riskFreeRate, strike);
        };

        var totalSpread = SpreadServiceParams.spread + minutesAvailableAdjustment(minutesAvailable, option);

        var response = new double[2];
        switch (side) {
            case BUY -> response[1] = option.ask(totalSpread) + qtyAdjustment(pricingId, qty, SpreadServiceParams.revealedImpactProbability);
            case SELL -> response[0] = option.bid(totalSpread + qtyAdjustment(pricingId, qty,
                    SpreadServiceParams.revealedImpactProbability));
            case MARKET -> {
                response[0] = option.bid(totalSpread + qtyAdjustment(pricingId, qty,
                        SpreadServiceParams.informedTradersImpactProbability));
                response[1] = option.ask(totalSpread + qtyAdjustment(pricingId, qty,
                        SpreadServiceParams.informedTradersImpactProbability));
            }
        }
        return response;
    }

    public double minutesAvailableAdjustment (Integer minutesAvailable, Option option) {
        var volatilityPerTimeUnit = volatility * minutesAvailable.doubleValue() / (StaticParams.tradingDays * StaticParams.tradingHours * StaticParams.minutesHour);
        return option.optionOnOptionPrice(volatilityPerTimeUnit);
    }

    public double qtyAdjustment (UUID pricingId, Integer qty, double probabilityOfChange) {
        var model = new GlostenMilgromModel(pricingId, SpreadServiceParams.fractionNoiseTraders, probabilityOfChange);
        return model.optimalSpreadDecimal() * qty / averageQty;
    }

    // expected to be static, QuotationService-related only
    public enum SIDE {
        BUY,
        SELL,
        MARKET
    }
}
