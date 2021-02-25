package de.unileipzig.irpact.commons.distattr;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface AttributableUnivariateDoubleDistribution extends AttributableUnivariateDistribution<Number>, UnivariateDoubleDistribution {

    @Override
    UnivariateDoubleDistribution getValue();
}
