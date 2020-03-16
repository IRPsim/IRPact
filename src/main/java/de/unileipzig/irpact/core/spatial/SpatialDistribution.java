package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.distribution.Distribution;

/**
 * @author Daniel Abitz
 */
public interface SpatialDistribution extends Distribution {

    SpatialInformation drawValue();
}
