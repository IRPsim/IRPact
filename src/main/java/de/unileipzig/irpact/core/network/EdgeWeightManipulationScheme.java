package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface EdgeWeightManipulationScheme extends Scheme {

    double getEdgeWeight(
            SimulationEnvironment environment,
            AgentGraph.Node source,
            AgentGraph.Node target,
            EdgeType edgeType
    );
}
