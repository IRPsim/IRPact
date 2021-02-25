package de.unileipzig.irpact.core.agent;

/**
 * @author Daniel Abitz
 */
public interface ProxyAgent<A extends Agent> extends Agent {

    boolean isSynced();

    A getRealAgent();
}
