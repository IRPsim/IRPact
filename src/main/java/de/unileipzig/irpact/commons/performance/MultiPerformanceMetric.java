package de.unileipzig.irpact.commons.performance;

/**
 * @author Daniel Abitz
 */
public interface MultiPerformanceMetric extends PerformanceMetric {

    double calcMulti(double[] trueValues, double[]... forecastedValues);
}
