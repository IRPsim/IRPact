package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class BasicSocialNetwork implements SocialNetwork {

    protected SocialGraph graph = new BasicSocialGraph();
    protected GraphConfiguration configuration;

    public BasicSocialNetwork() {
    }

    public void setGraph(SocialGraph graph) {
        this.graph = graph;
    }

    @Override
    public SocialGraph getGraph() {
        return graph;
    }

    public void setConfiguration(GraphConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GraphConfiguration getConfiguration() {
        return configuration;
    }
}
