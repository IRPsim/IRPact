package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public interface SpatialAgent extends Agent {

    SpatialInformation getSpatialInformation();
}
