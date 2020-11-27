package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Set;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface AgentGroup<T extends Agent> extends SimulationEntity {

    Set<T> getAgents();
}
