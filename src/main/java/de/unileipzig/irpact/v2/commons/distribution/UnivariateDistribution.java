package de.unileipzig.irpact.v2.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDistribution extends Distribution {

    double drawValue();
}
