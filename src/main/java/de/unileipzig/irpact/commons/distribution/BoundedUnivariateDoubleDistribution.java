package de.unileipzig.irpact.commons.distribution;

/**
 * @author Daniel Abitz
 */
public interface BoundedUnivariateDoubleDistribution extends UnivariateDoubleDistribution {

    double getLowerBound();

    double getUpperBound();

    boolean isLowerBoundInclusive();

    boolean isUpperBoundInclusive();
}
