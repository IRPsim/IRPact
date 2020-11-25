package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public interface SpatialAgent extends Agent {

    SpatialInformation getSpatialInformation();
}
