package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.dev.ToDo;
import de.unileipzig.irpact.core.network.EdgeType;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
@ToDo("ueberarbeiten, gefaelllt mir noch nicht")
public class UnchangingEdgeWeight implements EdgeWeightManipulationScheme {

    public static final String NAME = UnchangingEdgeWeight.class.getSimpleName();
    public static final UnchangingEdgeWeight INSTANCE = new UnchangingEdgeWeight();

    @Override
    public double getEdgeWeight(
            SimulationEnvironment environment,
            SocialGraph.Node source,
            SocialGraph.Node target,
            EdgeType edgeType) {
        return environment.getAgentNetwork()
                .getGraph()
                .findEdge(source, target, edgeType)
                .getWeight();
    }
}
