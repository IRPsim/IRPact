package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicInitializationData implements InitializationData {

    protected Map<ConsumerAgentGroup, Integer> agentCount;
    protected int startYear;
    protected int endYearInclusive = -1; //inclusive, z.b. 2015-2016 -> 2015 UND 2016
    protected boolean ignorePersistCheck = false;

    public BasicInitializationData() {
        this(new LinkedHashMap<>());
    }

    public BasicInitializationData(
            Map<ConsumerAgentGroup, Integer> agentCount) {
        this.agentCount = agentCount;
    }

    public void copyFrom(BasicInitializationData other) {
        if(this == other) {
            throw new IllegalStateException("self reference");
        }

        startYear = other.startYear;
        endYearInclusive = other.endYearInclusive;
        ignorePersistCheck = other.ignorePersistCheck;
    }

    //=========================
    //general
    //=========================

    public void setIgnorePersistenceCheckResult(boolean value) {
        ignorePersistCheck = value;
    }

    @Override
    public boolean ignorePersistenceCheckResult() {
        return ignorePersistCheck;
    }

    //=========================
    //time
    //=========================

    @Override
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    @Override
    public int getStartYear() {
        return startYear;
    }

    @Override
    public void setEndYear(int endYear) {
        this.endYearInclusive = endYear;
    }

    @Override
    public int getEndYear() {
        return endYearInclusive;
    }

    @Override
    public boolean hasValidEndYear() {
        return endYearInclusive > startYear;
    }

    //=========================
    //population size
    //=========================

    @Override
    public boolean hasInitialNumberOfConsumerAgents(ConsumerAgentGroup group) {
        return agentCount.containsKey(group);
    }

    @Override
    public void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int size) {
        agentCount.put(group, size);
    }

    @Override
    public int getInitialNumberOfConsumerAgents(ConsumerAgentGroup group) {
        Integer count = agentCount.get(group);
        if(count == null) {
            throw new NoSuchElementException(group.getName());
        }
        return count;
    }
}
