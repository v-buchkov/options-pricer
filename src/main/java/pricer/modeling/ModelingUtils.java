package pricer.modeling;

import java.util.Arrays;
import java.util.stream.IntStream;

public interface ModelingUtils {
    static double[] getReturnsFromPrices(double[] prices) {
        var returns = new double[prices.length - 1];
        IntStream.range(1, prices.length).forEach(i -> returns[i - 1] = prices[i] / prices[i - 1] - 1);
        return returns;
    }

    static double calculateStandardDeviation(double[] returns) {
        var meanReturn = Arrays.stream(returns).average().orElse(Double.NaN);

        var deviations = new double[returns.length - 1];
        IntStream.range(1, returns.length).forEach(i -> deviations[i] = Math.pow(returns[i] - meanReturn, 2));

        return Arrays.stream(deviations).average().orElse(Double.NaN);
    }
}
