package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.simulation.tasks.SimulationTask;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface InitializationData {

    void setStartYear(int year);

    void incrementStartYear();

    int getStartYear();

    void setEndYear(int year);

    int getEndYear();

    boolean hasValidEndYear();

    int getInitialNumberOfConsumerAgent(ConsumerAgentGroup group);

    void addTask(SimulationTask task);

    List<SimulationTask> getTasks();

    boolean ignorePersistenceCheckResult();
}
