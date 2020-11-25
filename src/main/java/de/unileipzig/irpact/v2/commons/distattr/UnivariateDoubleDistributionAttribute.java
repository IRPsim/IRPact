package de.unileipzig.irpact.v2.commons.distattr;

import de.unileipzig.irpact.v2.commons.attribute.DistributionAttribute;
import de.unileipzig.irpact.v2.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDoubleDistributionAttribute extends DistributionAttribute, UnivariateDoubleDistribution {

    @Override
    UnivariateDoubleDistribution getValue();
}
