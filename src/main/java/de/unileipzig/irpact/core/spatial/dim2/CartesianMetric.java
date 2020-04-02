package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.core.spatial.Metric;

/**
 * @author Daniel Abitz
 */
public enum CartesianMetric implements Metric {
    MANHATTEN,
    EUCLIDEAN,
    EUCLIDEAN2,
    MAXIMUM
}
