package de.unileipzig.irpact.v2.core.network;

/**
 * @author Daniel Abitz
 */
public interface SocialNetwork {

    SocialGraph getGraph();

    GraphConfiguration getConfiguration();
}
