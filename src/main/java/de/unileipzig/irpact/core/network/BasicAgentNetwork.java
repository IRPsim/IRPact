package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class BasicAgentNetwork implements AgentNetwork {

    private SocialNetwork graph;
    private GraphConfiguration graphConfiguration;

    public BasicAgentNetwork(
            SocialNetwork graph,
            GraphConfiguration graphConfiguration) {
        this.graph = graph;
        this.graphConfiguration = graphConfiguration;
    }

    @Override
    public SocialNetwork getGraph() {
        return graph;
    }

    @Override
    public GraphConfiguration getGraphConfiguration() {
        return graphConfiguration;
    }
}
