package pricer.modeling;

public interface ModelingParams {
    double spotChangeDecimal = 0.01;
    double volatilityChange = 0.01;
    double riskFreeRateChange = 0.0001;
    double timeChangeAnnualized = 0.0001;

    Integer volatilitySignificantDays = 30;

    Integer numberPaths = 20000;
    double daysTradingYear = 252;
}
