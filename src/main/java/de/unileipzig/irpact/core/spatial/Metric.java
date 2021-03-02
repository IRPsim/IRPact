package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface Metric extends IsEquals {

    double distance(SpatialInformation from, SpatialInformation to);
}
