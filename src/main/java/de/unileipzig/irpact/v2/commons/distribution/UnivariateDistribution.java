package de.unileipzig.irpact.v2.commons.distribution;

/**
 * @author Daniel Abitz
 * @param <T>
 */
public interface UnivariateDistribution<T> extends Distribution {

    T drawValue();
}
