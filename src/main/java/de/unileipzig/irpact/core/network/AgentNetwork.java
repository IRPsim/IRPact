package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public interface AgentNetwork {

    SocialNetwork getGraph();

    GraphConfiguration getGraphConfiguration();
}
