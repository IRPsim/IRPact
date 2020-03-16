package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class BasicAgentNetwork implements AgentNetwork {

    private AgentGraph graph;
    private GraphConfiguration graphConfiguration;

    public BasicAgentNetwork(
            AgentGraph graph,
            GraphConfiguration graphConfiguration) {
        this.graph = graph;
        this.graphConfiguration = graphConfiguration;
    }

    @Override
    public AgentGraph getGraph() {
        return graph;
    }

    @Override
    public GraphConfiguration getGraphConfiguration() {
        return graphConfiguration;
    }
}
