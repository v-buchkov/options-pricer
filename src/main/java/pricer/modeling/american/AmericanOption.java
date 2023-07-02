package pricer.modeling.american;

import pricer.modeling.ModelingParams;
import pricer.modeling.MonteCarlo;
import pricer.modeling.Option;

import java.util.Arrays;
import java.util.UUID;

// TODO: ParametrizedTest J-Unit
public abstract class AmericanOption extends Option {
    private final Option baseOption;

    public AmericanOption(UUID pricingId, double term, double volatility, double currentSpot, double initialSpot,
                          double riskFreeRate, double strike, Option referenceOption) {
        super(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike);
        baseOption = referenceOption;
    }

    public double optionPremium(double spot, double term, double riskFreeRate, double volatility) {
        var monteCarloProcess = new MonteCarlo(spot, term, riskFreeRate, volatility);
        var spotPaths = monteCarloProcess.GeometricBrownianMotionSimulation(ModelingParams.numberPaths);

        var optionPremiums = new double[ModelingParams.numberPaths];
        for(int onePath = 0; onePath < ModelingParams.numberPaths; onePath++) {
            for(Integer i = 0; i < spotPaths[onePath].length; i++){
                var spotUsed = spotPaths[onePath][i];

                var payoff = this.getPayoff(spotUsed);
                var baseOptionPremium = baseOption.optionPremium(spotUsed);

                if (i.equals(spotPaths[onePath].length - 1) || payoff > baseOptionPremium) {
                    optionPremiums[onePath] = payoff;
                    break;
                }
            }
        }
        return Arrays.stream(optionPremiums).average().orElse(Double.NaN);
    }

    public double getPayoff(double finalSpot) {
        return baseOption.getPayoff(finalSpot);
    }

    public double optionPremium(double spot) {
        return optionPremium(spot, term, riskFreeRate, volatility);
    }

    public double delta(double spot) {
        var spotStart = spot * Math.exp(-ModelingParams.spotChangeDecimal);
        var spotEnd = spot * Math.exp(ModelingParams.spotChangeDecimal);
        var optionPriceChange = optionPremium(spotEnd, term, riskFreeRate, volatility) -
                optionPremium(spotStart, term, riskFreeRate, volatility);
        return optionPriceChange / (spotEnd - spotStart);
    }

    public double delta() {
        return delta(currentSpot);
    }

    public double gamma() {
        var spotStart = currentSpot * Math.exp(-ModelingParams.spotChangeDecimal);
        var spotEnd = currentSpot * Math.exp(ModelingParams.spotChangeDecimal);
        var deltaChange = delta(spotEnd) - delta(spotStart);
        return deltaChange / (spotEnd - spotStart);
    }

    public double vega() {
        var volStart = volatility - ModelingParams.volatilityChange;
        var volEnd = volatility + ModelingParams.volatilityChange;
        var optionPriceChange = optionPremium(currentSpot, term, riskFreeRate, volEnd) -
                optionPremium(currentSpot, term, riskFreeRate, volStart);
        return optionPriceChange / (volEnd - volStart);
    }

    public double theta() {
        var termStart = term - ModelingParams.timeChangeAnnualized;
        var termEnd = term + ModelingParams.timeChangeAnnualized;
        var optionPriceChange = optionPremium(currentSpot, termEnd, riskFreeRate, volatility) -
                optionPremium(currentSpot, termStart, riskFreeRate, volatility);
        return optionPriceChange / (termEnd - termStart);
    }

    public double rho() {
        var rateStart = riskFreeRate - ModelingParams.riskFreeRateChange;
        var rateEnd = riskFreeRate + ModelingParams.riskFreeRateChange;
        var optionPriceChange = optionPremium(currentSpot, term, rateEnd, volatility) -
                optionPremium(currentSpot, term, rateStart, volatility);
        return optionPriceChange / (rateEnd - rateStart);
    }

    public double optionOnOptionPrice(double volatilityPerTimeUnit) {
        return 0.05;
    }
}
