package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDistribution extends Distribution {

    double drawValue();
}
