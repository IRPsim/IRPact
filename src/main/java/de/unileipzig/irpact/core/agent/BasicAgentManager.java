package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
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
    protected Map<ConsumerAgentGroup, Integer> agentCount;

    public BasicAgentManager() {
        this(new HashMap<>(), new HashMap<>());
    }

    public BasicAgentManager(Map<String, ConsumerAgentGroup> consumerAgentGroups, Map<ConsumerAgentGroup, Integer> agentCount) {
        this.consumerAgentGroups = consumerAgentGroups;
        this.agentCount = agentCount;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() {
        for(ConsumerAgentGroup cag: getConsumerAgentGroups()) {
            int count = getInitialNumberOfConsumerAgent(cag);
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

    @Override
    public void setInitialNumberOfConsumerAgents(ConsumerAgentGroup group, int count) {
        agentCount.put(group, count);
    }

    @Override
    public int getInitialNumberOfConsumerAgent(ConsumerAgentGroup group) {
        return agentCount.get(group);
    }

    @Override
    public void replacePlaceholder(ConsumerAgent realAgent) throws IllegalStateException {
        ConsumerAgentGroup cag = realAgent.getGroup();
        cag.replacePlaceholder(realAgent);

        environment.getNetwork()
                .getGraph()
                .replacePlaceholder(realAgent);
    }
}
