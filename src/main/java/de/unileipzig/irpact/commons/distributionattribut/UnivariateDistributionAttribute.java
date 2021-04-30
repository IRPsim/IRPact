package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;

/**
 * Combines attribute and univariate distribution.
 *
 * @author Daniel Abitz
 */
public interface UnivariateDistributionAttribute<T> extends DistributionAttribute<T>, UnivariateDistribution<T> {
}
