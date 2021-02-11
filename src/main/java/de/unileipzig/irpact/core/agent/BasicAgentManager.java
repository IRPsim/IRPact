package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingType;
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
        this(new HashMap<>());
    }

    public BasicAgentManager(Map<String, ConsumerAgentGroup> consumerAgentGroups) {
        this.consumerAgentGroups = consumerAgentGroups;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() {
        InitializationData initData = environment.getInitializationData();
        for(ConsumerAgentGroup cag: getConsumerAgentGroups()) {
            int count = initData.getInitialNumberOfConsumerAgent(cag);
            for(int i = 0; i < count; i++) {
                ConsumerAgent ca = cag.deriveAgent();
                if(cag.addAgent(ca)) {
                    LOGGER.trace(LoggingType.INITIALIZATION, LoggingPart.AGENT, "created: {}", ca.getName());
                } else {
                    throw new IllegalStateException("adding agent '" + ca.getName() + "' failed, name already exists");
                }
            }
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
    public boolean add(ConsumerAgentGroup group) {
        if(consumerAgentGroups.containsKey(group.getName())) {
            return false;
        } else {
            consumerAgentGroups.put(group.getName(), group);
            return true;
        }
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
