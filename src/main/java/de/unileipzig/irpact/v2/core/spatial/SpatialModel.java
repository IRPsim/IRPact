package de.unileipzig.irpact.v2.core.spatial;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface SpatialModel extends SimulationEntity {

    double distance(SpatialInformation from, SpatialInformation to);
}
