package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.core.misc.Initialization;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface SpatialModel extends SimulationEntity, Initialization {

    double distance(SpatialInformation from, SpatialInformation to);
}
