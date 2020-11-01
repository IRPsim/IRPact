package de.unileipzig.irpact.v2.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface BoundedUnivariateDoubleDistribution extends UnivariateDoubleDistribution {

    double getLowerBound();

    double getUpperBound();
}
