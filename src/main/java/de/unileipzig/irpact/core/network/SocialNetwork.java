package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public interface SocialNetwork {

    SocialGraph getGraph();

    GraphConfiguration getConfiguration();
}
