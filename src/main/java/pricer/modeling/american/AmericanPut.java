package pricer.modeling.american;

import pricer.modeling.european.EuropeanPut;

import java.util.UUID;

public class AmericanPut extends AmericanOption {
    public AmericanPut(UUID pricingId, double term, double volatility, double currentSpot, double initialSpot,
                        double riskFreeRate, double strike) {
        super(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike,
                new EuropeanPut(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike));
    }
}
