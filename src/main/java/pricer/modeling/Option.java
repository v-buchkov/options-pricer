package pricer.modeling;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.apache.commons.math3.distribution.NormalDistribution;

import pricer.modeling.european.EuropeanCall;
import pricer.modeling.european.EuropeanPut;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "EUROPEAN", include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EuropeanCall.class, name = "EUROPEAN_CALL"),
        @JsonSubTypes.Type(value = EuropeanPut.class, name = "EUROPEAN_PUT")
})
public abstract class Option {
    // id of request
    public final UUID pricingId;

    // constructor variables
    public final double volatility;
    public final double currentSpot;
    public final double initialSpot;
    public final double riskFreeRate;
    public final double term;
    public final double strike;

    // public variable, calculated from the inputs
    public final double discountFactor;

    // variables, used for local methods only
    private final double termSquareRoot;
    private final double d1PDF;
    private final double d2PDF;
    private final double d1CDF;
    private final double d2CDF;

    public Option(UUID pricingId, double term, double volatility, double currentSpot, double initialSpot,
                  double riskFreeRate, double strike) {
        this.pricingId = pricingId;
        this.term = term;
        this.volatility = volatility;
        this.currentSpot = currentSpot;
        this.initialSpot = initialSpot;
        this.riskFreeRate = riskFreeRate;
        this.strike = strike;

        this.termSquareRoot = Math.pow(term, 0.5);
        this.discountFactor = Math.exp(-riskFreeRate * term);

        // local variables - needed for PDF and CDF calculation only
        double d1 = (Math.log(currentSpot / strike) + (riskFreeRate + Math.pow(volatility, 2) / 2) * term) /
                (volatility + this.termSquareRoot);
        double d2 = d1 - volatility * this.termSquareRoot;

        var standardNormal = new NormalDistribution();
        this.d1PDF = standardNormal.probability(d1);
        this.d2PDF = standardNormal.probability(d2);
        this.d1CDF = standardNormal.cumulativeProbability(d1);
        this.d2CDF = standardNormal.cumulativeProbability(d2);
    }

    // each option is defined by the premium formula, set of greeks and payoff function
    public abstract double optionPremium(double spot);

    public double optionPremium() {
        return optionPremium(currentSpot);
    }

    public abstract double delta();
    public abstract double gamma();
    public abstract double vega();
    public abstract double theta();
    public abstract double rho();

    public abstract double getPayoff(double finalSpot);

    public double getPayoff() {
        return getPayoff(currentSpot);
    }

    public abstract double optionOnOptionPrice(double volatilityPerTimeUnit);

    // quotation formulas
    public double price() {
        return optionPremium() / initialSpot;
    }

    public double bid(double spreadFromMidPrice) {
        assert spreadFromMidPrice >= 0;
        return price() * (1 - spreadFromMidPrice);
    }

    public double ask(double spreadFromMidPrice) {
        assert spreadFromMidPrice >= 0;
        return price() * (1 + spreadFromMidPrice);
    }

    // PnL after executing an option
    public double finalPnL(double finalSpot, double commissionPaid) {
        return getPayoff(finalSpot) / initialSpot - (price() + commissionPaid) * Math.exp(riskFreeRate * term);
    }

    // formulas of call option - everything else is derived from this formulas
    public double europeanCallPremium(double spot) {
        return spot * d1CDF - d2CDF * strike * discountFactor;
    }

    public double europeanCallPremium() {
        return europeanCallPremium(currentSpot);
    }

    public double europeanCallDelta() {
        return d1CDF;
    }

    public double europeanCallGamma(double spot) {
        return d1PDF / (spot * volatility * term);
    }

    public double europeanCallGamma() {
        return europeanCallGamma(currentSpot);
    }

    public double europeanCallVega(double spot) {
        return spot * termSquareRoot * d1PDF;
    }

    public double europeanCallVega() {
        return europeanCallVega(currentSpot);
    }

    public double europeanCallTheta(double spot) {
        return -(spot * d1PDF * volatility) / (2 * termSquareRoot) -
                riskFreeRate * strike * discountFactor * d2CDF;
    }

    public double europeanCallTheta() {
        return europeanCallTheta(currentSpot);
    }

    public double europeanCallRho() {
        return strike * term * discountFactor * d2CDF;
    }
}
