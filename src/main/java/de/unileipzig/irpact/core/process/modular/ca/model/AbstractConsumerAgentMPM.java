package de.unileipzig.irpact.core.process.modular.ca.model;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.modular.ModularProcessPlan;
import de.unileipzig.irpact.core.process.modular.ca.SimpleConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentMPM extends SimulationEntityBase implements ConsumerAgentMPM, LoggingHelper {

    protected ConsumerAgentEvaluationModule startModule;

    protected AbstractConsumerAgentMPM() {
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    public void setStartModule(ConsumerAgentEvaluationModule startModule) {
        this.startModule = startModule;
        startModule.handleMissingParameters(this);
    }

    @Override
    public ConsumerAgentEvaluationModule getStartModule() {
        return startModule;
    }

    @Override
    public void handleNewProduct(Product newProduct) {
        getStartModule().handleNewProduct(newProduct);
    }

    @Override
    public ModularProcessPlan newPlan(Agent agent, Need need, Product product) {
        trace("[{}] create new plan for agent={}, need={}, product={}", getName(), agent.getName(), need.getName(), product.getName());

        ConsumerAgent ca = validate(agent);

        SimpleConsumerAgentData data = new SimpleConsumerAgentData();
        data.setAgent(ca);
        data.setModel(this);
        data.setProduct(product);
        data.setNeed(need);
        data.setRnd(deriveNewRnd());

        peekNewPlan(data);

        return data;
    }

    protected void peekNewPlan(SimpleConsumerAgentData data) {
    }

    protected static ConsumerAgent validate(Agent agent) {
        if(agent instanceof ConsumerAgent) {
            return (ConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires ConsumerAgent");
        }
    }
}
