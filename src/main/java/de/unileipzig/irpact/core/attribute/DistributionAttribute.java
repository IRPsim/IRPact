package de.unileipzig.irpact.core.attribute;

import de.unileipzig.irpact.commons.distribution.Distribution;

/**
 * @author Daniel Abitz
 */
public interface DistributionAttribute extends AttributeBase {

    Distribution getDistribution();
}
