package de.unileipzig.irpact.v2.core.network.topology;

import de.unileipzig.irpact.v2.core.network.SocialGraph;
import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopology implements GraphTopologyScheme {

    protected SocialGraph.Type edgeType;

    public UnlinkedGraphTopology(SocialGraph.Type edgeType) {
        this.edgeType = edgeType;
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        graph.removeAllEdges(edgeType);
    }
}
