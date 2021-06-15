package de.unileipzig.irpact.core.spatial.distribution;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public interface SpatialDistribution extends UnivariateDistribution<SpatialInformation> {

    boolean setUsed(SpatialInformation information);
}
