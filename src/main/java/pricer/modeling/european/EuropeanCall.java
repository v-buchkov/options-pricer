package pricer.modeling.european;

import java.util.UUID;

import pricer.modeling.Option;

// TODO: move annotation to controller
//@JsonTypeName(QuotationService.INSTRUMENT.EUROPEAN_CALL.name())
public class EuropeanCall extends Option {
    public EuropeanCall(UUID pricingId, double term, double volatility, double currentSpot, double initialSpot,
                        double riskFreeRate, double strike) {
        super(pricingId, term, volatility, currentSpot, initialSpot, riskFreeRate, strike);
    }

    public double optionPremium(double spot) {
        return this.europeanCallPremium(spot);
    }

    public double delta() {
        return this.europeanCallDelta();
    }

    public double gamma() {
        return this.europeanCallGamma();
    }

    public double vega() {
        return this.europeanCallVega();
    }

    public double theta() {
        return this.europeanCallTheta();
    }

    public double rho() {
        return this.europeanCallRho();
    }

    public double getPayoff(double finalSpot) {
        return Math.max(finalSpot - strike, 0.0);
    }

    public double optionOnOptionPrice(double volatilityPerTimeUnit) {
        return 0.05;
    }
}
