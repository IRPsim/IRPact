package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Metric extends ChecksumComparable {

    double distance(SpatialInformation from, SpatialInformation to);
}
