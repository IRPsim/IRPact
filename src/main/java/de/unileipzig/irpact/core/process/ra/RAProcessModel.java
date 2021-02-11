package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.MockConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class RAProcessModel implements ProcessModel {

    protected SimulationEnvironment environment;
    protected RAModelData modelData;
    protected Rnd rnd;

    public RAProcessModel() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setModelData(RAModelData modelData) {
        this.modelData = modelData;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    @Override
    public void onNewSimulationPeriod() {
        AgentManager agentManager = environment.getAgents();
        agentManager.streamConsumerAgents()
                .forEach(this::setupAgentForNewPeriod);
    }

    protected void setupAgentForNewPeriod(ConsumerAgent agent) {
        double renovationRate = RAProcessPlan.getRenovationRate(agent);
        boolean doRenovation = rnd.nextDouble() < renovationRate;
        setUnderRenovation(agent, doRenovation);

        double constructionRate = RAProcessPlan.getConstructionRate(agent);
        boolean doConstruction = rnd.nextDouble() < constructionRate;
        setUnderConstruction(agent, doConstruction);
    }

    @Override
    public ProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = cast(agent);
        Rnd rnd = environment.getSimulationRandom().createNewRandom();
        return new RAProcessPlan(environment, modelData, rnd, cAgent, need, product);
    }

    protected static ConsumerAgent cast(Agent agent) {
        if(agent instanceof ConsumerAgent) {
            return (ConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires ConsumerAgent");
        }
    }

    //=========================
    //util
    //=========================

    protected void setUnderConstruction(ConsumerAgent agent, boolean value) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        if(attr == null) {
            BasicConsumerAgentAttribute battr = new BasicConsumerAgentAttribute();
            battr.setName(RAConstants.UNDER_CONSTRUCTION);
            battr.setDoubleValue(value ? 1.0 : 0.0);
            battr.setGroup(MockConsumerAgentGroupAttribute.INSTANCE);
            agent.addAttribute(battr);
        } else {
            attr.setDoubleValue(value ? 1.0 : 0.0);
        }
    }

    protected void setUnderRenovation(ConsumerAgent agent, boolean value) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        if(attr == null) {
            BasicConsumerAgentAttribute battr = new BasicConsumerAgentAttribute();
            battr.setName(RAConstants.UNDER_RENOVATION);
            battr.setDoubleValue(value ? 1.0 : 0.0);
            battr.setGroup(MockConsumerAgentGroupAttribute.INSTANCE);
            agent.addAttribute(battr);
        } else {
            attr.setDoubleValue(value ? 1.0 : 0.0);
        }
    }
}
