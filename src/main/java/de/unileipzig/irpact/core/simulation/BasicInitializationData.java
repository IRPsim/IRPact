package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.simulation.tasks.SimulationTask;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicInitializationData implements InitializationData {

    protected Map<ConsumerAgentGroup, Integer> agentCount;
    protected List<SimulationTask> tasks;
    protected int startYear;
    protected int endYear = -1; //inclusive, z.b. 2015-2016 -> 2015 UND 2016
    protected boolean ignorePersistCheck = false;

    public BasicInitializationData() {
        this(new LinkedHashMap<>(), new ArrayList<>());
    }

    public BasicInitializationData(
            Map<ConsumerAgentGroup, Integer> agentCount,
            List<SimulationTask> tasks) {
        this.agentCount = agentCount;
        this.tasks = tasks;
    }

    public void copyFrom(BasicInitializationData other) {
        if(this == other) {
            throw new IllegalStateException("self reference");
        }

        tasks.addAll(other.tasks);
        startYear = other.startYear;
        endYear = other.endYear;
        ignorePersistCheck = other.ignorePersistCheck;
    }

    public void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int count) {
        agentCount.put(group, count);
    }

    @Override
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    @Override
    public void incrementStartYear() {
        startYear++;
    }

    @Override
    public int getStartYear() {
        return startYear;
    }

    @Override
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public int getEndYear() {
        return endYear;
    }

    @Override
    public boolean hasValidEndYear() {
        return endYear > startYear;
    }

    @Override
    public int getInitialNumberOfConsumerAgent(ConsumerAgentGroup group) {
        return agentCount.get(group);
    }

    @Override
    public void addTask(SimulationTask task) {
        tasks.add(task);
    }

    @Override
    public List<SimulationTask> getTasks() {
        return tasks;
    }

    public void setIgnorePersistenceCheckResult(boolean value) {
        ignorePersistCheck = value;
    }

    @Override
    public boolean ignorePersistenceCheckResult() {
        return ignorePersistCheck;
    }
}
