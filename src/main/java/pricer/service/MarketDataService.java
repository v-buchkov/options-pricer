package pricer.service;

import org.springframework.stereotype.Service;
import pricer.persistence.repository.FortsOptDealRepository;
import pricer.persistence.repository.RiskFreeRateRepository;
import pricer.persistence.repository.SpotPriceRepository;

import static pricer.modeling.ModelingUtils.calculateStandardDeviation;
import static pricer.modeling.ModelingUtils.getReturnsFromPrices;

// TODO: LiquidBase
@Service
public class MarketDataService {
    public final FortsOptDealRepository fortsOptDealRepository;
    public final SpotPriceRepository spotPriceRepository;
    public final RiskFreeRateRepository riskFreeRateRepository;

    public MarketDataService(FortsOptDealRepository fortsOptDealRepository, SpotPriceRepository spotPriceRepository,
                             RiskFreeRateRepository riskFreeRateRepository) {
        this.fortsOptDealRepository = fortsOptDealRepository;
        this.spotPriceRepository = spotPriceRepository;
        this.riskFreeRateRepository = riskFreeRateRepository;
    }

    public double getCurrentRiskFreeRate(QuotationEnums.BASE_ASSET baseAsset) {
        return riskFreeRateRepository.getCurrentRiskFreeRate(baseAsset.getQuotationCurrency());
    }

    public double getCurrentSpot(QuotationEnums.BASE_ASSET baseAsset) {
        return spotPriceRepository.getCurrentSpot(baseAsset.name());
    }

    // TODO: LocalDateTime conversion, unified dates
    public double getAverageQty(QuotationEnums.BASE_ASSET baseAsset, String dateStart, String dateEnd) {
        return fortsOptDealRepository.getAverageQtyForPeriod(baseAsset.moexTicker, dateStart, dateEnd);
    }

    // Database ENUM (?)
    public double getHistoricalVolatility(QuotationEnums.BASE_ASSET baseAsset, String dateStart, String dateEnd) {
        var prices = spotPriceRepository.getSpotPricesForPeriod(baseAsset.toString(), dateStart, dateEnd);
        var returns = getReturnsFromPrices(prices);

        return calculateStandardDeviation(returns);
    }

}
