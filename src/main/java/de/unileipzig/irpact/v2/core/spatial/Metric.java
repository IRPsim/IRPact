package de.unileipzig.irpact.v2.core.spatial;

/**
 * @author Daniel Abitz
 */
public interface Metric {

    double distance(SpatialInformation from, SpatialInformation to);
}
