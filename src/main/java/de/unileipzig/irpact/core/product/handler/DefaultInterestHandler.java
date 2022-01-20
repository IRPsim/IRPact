package de.unileipzig.irpact.core.product.handler;

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
public class DefaultInterestHandler extends AbstractNewProductHandler implements LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultInterestHandler.class);

    protected String interestAttributeName;

    public DefaultInterestHandler() {
    }

    public void setInterestAttributeName(String interestAttributeName) {
        this.interestAttributeName = interestAttributeName;
    }

    public String getInterestAttributeName() {
        return interestAttributeName;
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
                initalizeInitialProductInterest(environment, ca, product);
            }
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

    protected double getInitialProductInterest(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return environment.getAttributeHelper().getDouble(agent, product, getInterestAttributeName(), true);
    }
}
