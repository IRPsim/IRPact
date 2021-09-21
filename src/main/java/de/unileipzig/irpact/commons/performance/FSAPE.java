package de.unileipzig.irpact.commons.performance;

/**
 * @author Daniel Abitz
 */
public final class FSAPE extends AbstractPerformanceMetric implements MultiPerformanceMetric {

    public static final FSAPE INSTANCE = new FSAPE();

    public FSAPE() {
    }

    @Override
    public double calc(double[] trueValues, double[] forecastedValues) {
        return calcMulti(trueValues, forecastedValues);
    }

    @Override
    public double calcMulti(double[] trueValues, double[]... forecastedValues) {
        validate(trueValues, forecastedValues);

        double sum = 0.0;
        for(int i = 0; i < trueValues.length; i++) {
            sum += eval(i, trueValues, forecastedValues);
        }
        return sum;
    }

    private static int eval(int i, double[] trueValues, double[]... forecastedValues) {
        double eij = 0;
        for(int j = 0; j < forecastedValues.length; j++) {
            if(j == 0) {
                eij = calcEij(i, trueValues, forecastedValues[j]);
            } else {
                if(eij != calcEij(i, trueValues, forecastedValues[j])) {
                    return 0;
                }
            }
        }
        return 1;
    }

    private static double calcEij(int i, double[] trueValues, double[] forecastedValues) {
        return Math.abs(trueValues[i] - forecastedValues[i]);
    }
}
