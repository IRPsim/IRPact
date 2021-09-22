package de.unileipzig.irpact.commons.performance;

/**
 * @author Daniel Abitz
 */
public final class RMSD extends AbstractPerformanceMetric {

    public static final RMSD INSTANCE = new RMSD();

    public RMSD() {
    }

    public double calcSquare(double[] x1, double[] x2) {
        validate(x1, x2);

        double sum = 0.0;
        for(int i = 0; i < x1.length; i++) {
            double temp = x1[i] - x2[i];
            sum += temp * temp / x1.length;
        }
        return sum;
    }

    @Override
    public double calc(double[] trueValues, double[] forecastedValues) {
        return Math.sqrt(calcSquare(trueValues, forecastedValues));
    }
}
