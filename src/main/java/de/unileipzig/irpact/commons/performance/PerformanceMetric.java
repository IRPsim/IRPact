package de.unileipzig.irpact.commons.performance;

/**
 * @author Daniel Abitz
 */
public interface PerformanceMetric {

    double calc(double[] trueValues, double[] forecastedValues);
}
