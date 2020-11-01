package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDistributionAttribute extends DistributionAttribute {

    @Override
    UnivariateDoubleDistribution getDistribution();

    double drawValue();
}
