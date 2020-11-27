package de.unileipzig.irpact.commons.distattr;

import de.unileipzig.irpact.commons.attribute.DistributionAttribute;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDoubleDistributionAttribute extends DistributionAttribute, UnivariateDoubleDistribution {

    @Override
    UnivariateDoubleDistribution getValue();
}
