package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface SpatialModel extends SimulationEntity {

    double distance(SpatialInformation from, SpatialInformation to);
}
