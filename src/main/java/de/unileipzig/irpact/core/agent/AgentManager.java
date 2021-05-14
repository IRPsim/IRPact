package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.util.IdManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.develop.Todo;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
//Listener support fuer neue Gruppen?
@Todo("ProcessPlan auch Agenten austauschen!!! -> wenn er dem Jadexagenten uebergeben wird")
public interface AgentManager extends InitalizablePart {

    //=========================
    //general
    //=========================

    IdManager getAttentionOrderManager();

    Collection<ConsumerAgentGroup> getConsumerAgentGroups();

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

    void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping);
}
