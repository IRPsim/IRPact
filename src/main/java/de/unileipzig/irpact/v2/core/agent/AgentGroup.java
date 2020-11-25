package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface AgentGroup<T extends Agent> extends SimulationEntity {

    Set<T> getAgents();
}
