package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distribution.Distribution;

/**
 * @author Daniel Abitz
 */
public interface DistributionAttribute extends Attribute {

    Distribution getDistribution();
}
