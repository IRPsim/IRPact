package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * Combines attribute and univariate double distribution.
 *
 * @author Daniel Abitz
 */
public interface AttributableUnivariateDoubleDistribution extends AttributableUnivariateDistribution<Number>, UnivariateDoubleDistribution {
}
