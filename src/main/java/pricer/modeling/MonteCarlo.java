package pricer.modeling;

import org.apache.commons.math3.distribution.NormalDistribution;

import static pricer.modeling.ModelingParams.daysTradingYear;

public class MonteCarlo {
    // always used variables
    public final double spot;
    public final double term;
    public final double mean;
    public final double vol;

    public MonteCarlo(double spot, double term, double mean, double vol) {
        this.spot = spot;
        this.term = term;
        this.mean = mean;
        this.vol = vol;
    }

    // TODO: cache
    public double[][] GeometricBrownianMotionSimulation(Integer numberPaths) {
        // calculate intervals for Monte Carlo simulation
        Integer totalDays = (int)Math.round(daysTradingYear * this.term);
        double timeChange = (double) 1 / totalDays;

        // log-normal mean
        double const_drift = (mean - 0.5 * Math.pow(vol, 2)) * timeChange;

        double[][] outputPaths = new double[2 * numberPaths][totalDays];
        for(int onePath = 0; onePath < numberPaths; onePath++) {
            outputPaths[onePath][0] = this.spot;
            outputPaths[numberPaths + onePath][0] = this.spot;
            for(int days=1; days < totalDays; days++) {
                var standardNormal = new NormalDistribution();
                double z = standardNormal.sample();
                double change = Math.exp(const_drift + vol * Math.pow(timeChange, 0.5) * z);
                outputPaths[onePath][days] = outputPaths[onePath][days - 1] * change;
                // mirroring due to symmetry of normal distribution
                outputPaths[numberPaths + onePath][days] = outputPaths[numberPaths + onePath][days - 1] * -change;
            }
        }
        return outputPaths;
    }
}
