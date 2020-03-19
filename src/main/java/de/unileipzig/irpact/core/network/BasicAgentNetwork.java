package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class BasicAgentNetwork implements AgentNetwork {

    private SocialGraph graph;
    private GraphConfiguration graphConfiguration;

    public BasicAgentNetwork(
            SocialGraph graph,
            GraphConfiguration graphConfiguration) {
        this.graph = graph;
        this.graphConfiguration = graphConfiguration;
    }

    @Override
    public SocialGraph getGraph() {
        return graph;
    }

    @Override
    public GraphConfiguration getGraphConfiguration() {
        return graphConfiguration;
    }
}
