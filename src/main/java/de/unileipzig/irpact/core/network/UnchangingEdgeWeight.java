package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.annotation.ToDo;
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
                .getEdge(source, target)
                .getWeight(edgeType);
    }
}
