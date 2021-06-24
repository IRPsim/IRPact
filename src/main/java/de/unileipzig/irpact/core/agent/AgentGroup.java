package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface AgentGroup<T extends Agent> extends SimulationEntity {

    Collection<T> getAgents();

    default Stream<T> streamAgents() {
        return getAgents().stream();
    }

    default int getNumberOfAgents() {
        return getAgents().size();
    }
}
