package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.InitializationData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicAgentManager implements AgentManager {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicAgentManager.class);

    protected SimulationEnvironment environment;
    protected Map<String, ConsumerAgentGroup> consumerAgentGroups;
    protected ConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();

    public BasicAgentManager() {
        this(new LinkedHashMap<>());
    }

    public BasicAgentManager(Map<String, ConsumerAgentGroup> consumerAgentGroups) {
        this.consumerAgentGroups = consumerAgentGroups;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                ChecksumComparable.getMapChecksum(consumerAgentGroups),
                affinityMapping.getChecksum()
        );
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() throws MissingDataException {
        InitializationData initData = environment.getInitializationData();
        for(ConsumerAgentGroup cag: getConsumerAgentGroups()) {
            checkConsumerAgentGroup(cag);
            int count = initData.getInitialNumberOfConsumerAgents(cag);
            int i = cag.getNumberOfAgents();
            LOGGER.debug("create {} agents for group '{}'", Math.max(count - i, 0), cag.getName());
            for(; i < count; i++) {
                ConsumerAgent ca = cag.deriveAgent();
                if(cag.addAgent(ca)) {
                    LOGGER.trace(IRPSection.INITIALIZATION_AGENT, "added agent '{}' to group '{}'", ca.getName(), cag.getName());
                } else {
                    throw new IllegalStateException("adding agent '" + ca.getName() + "' failed, name already exists");
                }
            }
        }
    }

    private void checkConsumerAgentGroup(ConsumerAgentGroup cag) throws MissingDataException {
        if(cag.getEnvironment() == null) {
            throw new MissingDataException("missing environment");
        }
    }

    @Override
    public void validate() throws ValidationException {
    }

    //=========================
    //general
    //=========================

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

    @Override
    public void setConsumerAgentGroupAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping) {
        this.affinityMapping = affinityMapping;
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
