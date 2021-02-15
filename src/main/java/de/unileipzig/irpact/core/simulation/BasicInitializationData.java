package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.simulation.tasks.SimulationTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicInitializationData implements InitializationData {

    protected Map<ConsumerAgentGroup, Integer> agentCount;
    protected List<SimulationTask> tasks;
    protected int startYear;
    protected int endYear = -1; //inclusive, z.b. 2015-2016 -> 2015 UND 2016


    public BasicInitializationData() {
        this(new HashMap<>(), new ArrayList<>());
    }

    public BasicInitializationData(
            Map<ConsumerAgentGroup, Integer> agentCount,
            List<SimulationTask> tasks) {
        this.agentCount = agentCount;
        this.tasks = tasks;
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
}
