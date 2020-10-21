package de.unileipzig.irpact.v2.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface BoundedUnivariateDistribution extends UnivariateDistribution {

    double getLowerBound();

    double getUpperBound();
}
