package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.network.topology.EdgeWeightManipulationScheme;
import de.unileipzig.irpact.core.network.topology.GraphTopologyScheme;
import de.unileipzig.irpact.core.network.topology.TopologyManipulationScheme;

/**
 * @author Daniel Abitz
 */
public interface GraphConfiguration {

    GraphTopologyScheme getGraphTopologyScheme();

    TopologyManipulationScheme getTopologyManipulationScheme();

    EdgeWeightManipulationScheme getEdgeWeightManipulationScheme();
}
