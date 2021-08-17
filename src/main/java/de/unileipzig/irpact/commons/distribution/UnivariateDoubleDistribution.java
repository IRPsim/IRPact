package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDoubleDistribution extends UnivariateDistribution<Number> {

    @Override
    default UnivariateDoubleDistribution copyDistribution() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Double drawValue() {
        return drawDoubleValue();
    }

    double drawDoubleValue();
}
