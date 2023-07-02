package pricer.modeling.european;

import java.util.UUID;

import pricer.modeling.Option;

public class EuropeanPut extends Option {
    public EuropeanPut(UUID pricingId, double term, double volatility, double currentSpot, double initialSpot,
                       double riskFreeRate, double strike) {
        super(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike);
    }

    public double optionPremium(double spot) {
        return this.europeanCallPremium(spot) - spot + strike * discountFactor;
    }

    public double delta() {
        return this.europeanCallDelta() - 1;
    }

    public double gamma() {
        return this.europeanCallGamma();
    }

    public double vega() {
        return this.europeanCallVega();
    }

    public double theta() {
        // Assuming that [dS/dt = 0], i.e. zero-drift stochastic process
        return this.europeanCallTheta() - strike * riskFreeRate * discountFactor;
    }

    public double rho() {
        return this.europeanCallRho() - strike * term * discountFactor;
    }

    public double getPayoff(double finalSpot) {
        return Math.max(strike - finalSpot, 0.0);
    }

    public double optionOnOptionPrice(double volatilityPerTimeUnit) {
        return 0.05;
    }
}
