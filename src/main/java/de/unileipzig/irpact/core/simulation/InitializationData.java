package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public interface InitializationData {

    //=========================
    //general
    //=========================

    boolean ignorePersistenceCheckResult();

    //=========================
    //time
    //=========================

    void setStartYear(int year);

    int getStartYear();

    void setEndYear(int year);

    int getEndYear();

    boolean hasValidEndYear();

    //=========================
    //population size
    //=========================

    boolean hasInitialNumberOfConsumerAgents(ConsumerAgentGroup group);

    void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int size);

    int getInitialNumberOfConsumerAgents(ConsumerAgentGroup group);
}
