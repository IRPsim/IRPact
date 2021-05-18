package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.persistence.annotation.ChecksumAndPersistentValue;
import de.unileipzig.irpact.commons.util.IdManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicAgentManager implements AgentManager {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicAgentManager.class);

    @ChecksumAndPersistentValue("id")
    protected final IdManager ATTENTION_ORDER = new IdManager(0L);

    protected SimulationEnvironment environment;
    @ChecksumAndPersistentValue("values")
    protected Map<String, ConsumerAgentGroup> consumerAgentGroups;
    @ChecksumAndPersistentValue
    protected ConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();

    public BasicAgentManager() {
        this(new LinkedHashMap<>());
    }

    public BasicAgentManager(Map<String, ConsumerAgentGroup> consumerAgentGroups) {
        this.consumerAgentGroups = consumerAgentGroups;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                ChecksumComparable.getMapChecksum(consumerAgentGroups),
                affinityMapping.getChecksum(),
                getAttentionOrderManager().lastId()
        );
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    //=========================
    //general
    //=========================

    @Override
    public IdManager getAttentionOrderManager() {
        return ATTENTION_ORDER;
    }

    @Override
    public Collection<ConsumerAgentGroup> getConsumerAgentGroups() {
        return consumerAgentGroups.values();
    }

    @Override
    public ConsumerAgentGroup getConsumerAgentGroup(String name) {
        return consumerAgentGroups.get(name);
    }

    @Override
    public boolean hasConsumerAgentGroup(String name) {
        return consumerAgentGroups.containsKey(name);
    }

    @Override
    public void addConsumerAgentGroup(ConsumerAgentGroup group) {
        if(hasConsumerAgentGroup(group.getName())) {
            throw new IllegalArgumentException("group name '" + group.getName() + "' already exists");
        }
        consumerAgentGroups.put(group.getName(), group);
    }

    public void addAllConsumerAgentGroups(Collection<? extends ConsumerAgentGroup> cags) {
        for(ConsumerAgentGroup cag: cags) {
            addConsumerAgentGroup(cag);
        }
    }

    @Override
    public void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping) {
        this.affinityMapping = affinityMapping;
    }

    @Override
    public boolean hasConsumerAgentGroupAffinityMapping() {
        return affinityMapping != null;
    }

    @Override
    public ConsumerAgentGroupAffinityMapping getConsumerAgentGroupAffinityMapping() {
        return affinityMapping;
    }

    @Override
    public int getTotalNumberOfConsumerAgents() {
        return getConsumerAgentGroups().stream()
                .mapToInt(AgentGroup::getNumberOfAgents)
                .sum();
    }
}
