package de.unileipzig.irpact.core.spatial;

/**
 * @author Daniel Abitz
 */
public interface Metric {

    double distance(SpatialInformation from, SpatialInformation to);
}
