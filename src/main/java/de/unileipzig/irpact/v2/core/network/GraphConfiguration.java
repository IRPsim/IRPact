package de.unileipzig.irpact.v2.core.network;

import de.unileipzig.irpact.v2.core.network.topology.GraphTopologyScheme;
import de.unileipzig.irpact.v2.core.network.topology.TopologyManipulationScheme;

/**
 * @author Daniel Abitz
 */
public interface GraphConfiguration {

    GraphTopologyScheme getGraphTopologyScheme();

    TopologyManipulationScheme getTopologyManipulationScheme();
}
