package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distribution.DistributionBase;

/**
 * @author Daniel Abitz
 */
public interface DistributionAttribute extends Attribute<DistributionBase> {

    DistributionBase getValue();
}
