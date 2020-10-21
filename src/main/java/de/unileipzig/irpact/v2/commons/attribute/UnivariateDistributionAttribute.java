package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distribution.UnivariateDistribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDistributionAttribute extends DistributionAttribute {

    @Override
    UnivariateDistribution getDistribution();

    double drawValue();
}
