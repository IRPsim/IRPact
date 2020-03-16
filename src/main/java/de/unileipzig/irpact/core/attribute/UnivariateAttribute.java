package de.unileipzig.irpact.core.attribute;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateAttribute extends DistributionAttribute {

    @Override
    UnivariateDistribution getDistribution();

    default double drawValue() {
        return getDistribution().drawValue();
    }
}