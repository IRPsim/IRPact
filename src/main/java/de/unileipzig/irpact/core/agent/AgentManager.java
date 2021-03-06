package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.IdManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.population.AgentPopulation;
import de.unileipzig.irpact.core.misc.InitalizablePart;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface AgentManager extends InitalizablePart {

    //=========================
    //general
    //=========================

    IdManager getAttentionOrderManager();

    Collection<ConsumerAgentGroup> getConsumerAgentGroups();

    default List<String> listAllConsumerAgentGroupNames() {
        return getConsumerAgentGroups().stream()
                .map(Nameable::getName)
                .collect(Collectors.toList());
    }

    boolean hasConsumerAgentGroup(String name);

    void addConsumerAgentGroup(ConsumerAgentGroup group);

    ConsumerAgentGroup getConsumerAgentGroup(String name);

    default ConsumerAgentGroup secureGetConsumerAgentGroup(String name) {
        ConsumerAgentGroup cag = getConsumerAgentGroup(name);
        if(cag == null) {
            throw new NoSuchElementException(name);
        }
        return cag;
    }

    default Stream<ConsumerAgent> streamConsumerAgents() {
        return getConsumerAgentGroups().stream()
                .flatMap(cag -> cag.getAgents().stream());
    }

    default Iterator<ConsumerAgent> iteratorConsumerAgents() {
        return streamConsumerAgents().iterator();
    }

    default Iterable<ConsumerAgent> iterableConsumerAgents() {
        //noinspection NullableProblems
        return this::iteratorConsumerAgents;
    }

    default Stream<ConsumerAgentGroup> streamConsumerAgentGroups() {
        return getConsumerAgentGroups().stream();
    }

    int getTotalNumberOfConsumerAgents();

    ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping();

    boolean hasConsumerAgentGroupAffinityMapping();

    void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping);

    //=========================
    //Population
    //=========================

    AgentPopulation getInitialAgentPopulation();
}
