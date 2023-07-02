package pricer.modeling.market_maker;

import java.util.UUID;

public class GlostenMilgromModel {
    // id of request
    public final UUID pricingId;

    public final double fractionNoiseTraders;
    public final double probabilityOfChange;

    private final double fractionInformedTraders;

    public GlostenMilgromModel(UUID pricingId, double fractionNoiseTraders, double probabilityOfChange) {
        this.pricingId = pricingId;
        this.fractionNoiseTraders = fractionNoiseTraders;
        this.probabilityOfChange = probabilityOfChange;

        this.fractionInformedTraders = 1 - fractionNoiseTraders;
    }

    public double optimalSpreadDecimal() {
        return probabilityOfChange * (1 + fractionInformedTraders) / (1 - fractionInformedTraders *
                (1 - 2 * probabilityOfChange)) - probabilityOfChange * (1 - fractionInformedTraders) /
                (1 + fractionInformedTraders * (1 - 2 * probabilityOfChange));
    }
}
