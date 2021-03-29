package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface SpatialModel extends SimulationEntity, InitalizablePart {

    double distance(SpatialInformation from, SpatialInformation to);
}
