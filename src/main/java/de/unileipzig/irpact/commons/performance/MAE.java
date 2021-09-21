package de.unileipzig.irpact.commons.performance;

/**
 * @author Daniel Abitz
 */
public final class MAE extends AbstractPerformanceMetric {

    public static final MAE INSTANCE = new MAE();

    public MAE() {
    }

    @Override
    public double calc(double[] trueValues, double[] forecastedValues) {
        validate(trueValues, forecastedValues);

        double sum = 0.0;
        for(int i = 0; i < trueValues.length; i++) {
            double temp = trueValues[i] - forecastedValues[i];
            sum += Math.abs(temp) / trueValues.length;
        }
        return sum;
    }
}
