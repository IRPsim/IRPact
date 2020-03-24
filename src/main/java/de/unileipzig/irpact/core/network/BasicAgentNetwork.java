package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class BasicAgentNetwork implements AgentNetwork {

    private SocialGraph socialGraph;
    private GraphConfiguration graphConfiguration;

    public BasicAgentNetwork(
            SocialGraph socialGraph,
            GraphConfiguration graphConfiguration) {
        this.socialGraph = socialGraph;
        this.graphConfiguration = graphConfiguration;
    }

    @Override
    public SocialGraph getGraph() {
        return socialGraph;
    }

    @Override
    public GraphConfiguration getGraphConfiguration() {
        return graphConfiguration;
    }
}
