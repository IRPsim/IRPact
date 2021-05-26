package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 * @param <T>
 */
public interface UnivariateDistribution<T> extends Distribution<T> {

    @Override
    default UnivariateDistribution<T> copyDistribution() {
        throw new UnsupportedOperationException();
    }
}
