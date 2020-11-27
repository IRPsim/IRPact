package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.distribution.DistributionBase;

/**
 * @author Daniel Abitz
 */
public interface DistributionAttribute extends Attribute<DistributionBase> {

    DistributionBase getValue();
}
