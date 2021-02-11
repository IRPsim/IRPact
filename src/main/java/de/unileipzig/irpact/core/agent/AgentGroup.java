package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Collection;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface AgentGroup<T extends Agent> extends SimulationEntity {

    Collection<T> getAgents();

    default int getNumberOfAgents() {
        return getAgents().size();
    }
}
