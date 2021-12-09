package de.unileipzig.irpact.core.product.handler;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultAwarenessInterestHandler extends AbstractNewProductHandler implements LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultAwarenessInterestHandler.class);

    protected String awarenessAttributeName;
    protected String interestAttributeName;
    protected Rnd rnd;

    public DefaultAwarenessInterestHandler() {
    }

    public void setAwarenessAttributeName(String awarenessAttributeName) {
        this.awarenessAttributeName = awarenessAttributeName;
    }

    public String getAwarenessAttributeName() {
        return awarenessAttributeName;
    }

    public void setInterestAttributeName(String interestAttributeName) {
        this.interestAttributeName = interestAttributeName;
    }

    public String getInterestAttributeName() {
        return interestAttributeName;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    @Override
    public void handleProduct(SimulationEnvironment environment, Product product) {
        trace("[{}] handle product '{}'", getName(), product.getName());
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                initalizeInitialProductAwareness(environment, ca, product, getRnd());
                initalizeInitialProductInterest(environment, ca, product);
            }
        }
    }

    protected void initalizeInitialProductAwareness(SimulationEnvironment environment, ConsumerAgent ca, Product fp, Rnd rnd) {
        if(ca.isAware(fp)) {
            trace("consumer agent '{}' already aware", ca.getName());
            return;
        }

        double chance = getInitialProductAwareness(environment, ca, fp);
        double draw = rnd.nextDouble();
        boolean isAware = draw < chance;
        trace("is consumer agent '{}' initial aware of product '{}'? {} ({} < {})", ca.getName(), fp.getName(), isAware, draw, chance);
        if(isAware) {
            ca.makeAware(fp);
        }
    }

    protected void initalizeInitialProductInterest(SimulationEnvironment environment, ConsumerAgent ca, Product fp) {
        double interest = getInitialProductInterest(environment, ca, fp);
        if(interest > 0) {
            trace("set awareness for consumer agent '{}' because initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
            ca.makeAware(fp);
        }
        ca.updateInterest(fp, interest);
        trace("consumer agent '{}' has initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
    }

    protected double getInitialProductAwareness(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return environment.getAttributeHelper().getDouble(agent, product, getAwarenessAttributeName(), true);
    }

    protected double getInitialProductInterest(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return environment.getAttributeHelper().getDouble(agent, product, getInterestAttributeName(), true);
    }
}
