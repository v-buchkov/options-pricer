package pricer.modeling.american;

import java.util.UUID;

import pricer.modeling.european.EuropeanCall;

public class AmericanCall extends AmericanOption {
    public AmericanCall(UUID pricingId, double term, double volatility, double currentSpot, double initialSpot,
                        double riskFreeRate, double strike) {
        super(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike,
                new EuropeanCall(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike));
    }
}
