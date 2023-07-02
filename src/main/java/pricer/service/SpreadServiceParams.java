package pricer.service;

public interface SpreadServiceParams {
    double spread = 0.005;

    double fractionNoiseTraders = 0.8;
    double informedTradersImpactProbability = 0.9;
    double revealedImpactProbability = 0.7;
}
