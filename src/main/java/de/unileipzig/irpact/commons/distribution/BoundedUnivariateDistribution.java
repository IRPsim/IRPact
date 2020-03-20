package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface BoundedUnivariateDistribution extends UnivariateDistribution {

    double getLowerBound();

    double getUpperBound();
}
