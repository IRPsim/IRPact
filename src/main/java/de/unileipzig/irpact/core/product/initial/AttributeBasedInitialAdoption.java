package de.unileipzig.irpact.core.product.initial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AttributeBasedInitialAdoption extends NameableBase implements NewProductHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AttributeBasedInitialAdoption.class);

    protected String attributeName;
    protected Rnd rnd;

    public AttributeBasedInitialAdoption() {
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public void handleProduct(SimulationEnvironment environment, Product product) {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                double chance = getInitialAdopter(environment, ca, product);
                double draw = rnd.nextDouble();
                boolean isAdopter = draw < chance;
                LOGGER.trace(
                        IRPSection.INITIALIZATION_PARAMETER,
                        "Is consumer agent '{}' initial adopter of product '{}'? {} ({} < {})",
                        ca.getName(), product.getName(), isAdopter, draw, chance
                );
                if(isAdopter) {
                    ca.adoptInitial(product);
                }
            }
        }
    }

    protected double getInitialAdopter(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return environment.getAttributeHelper().getDoubleValue(agent, product, getAttributeName());
    }
}
