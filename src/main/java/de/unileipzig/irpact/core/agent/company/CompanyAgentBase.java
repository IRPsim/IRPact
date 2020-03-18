package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.InformationAgentBase;
import de.unileipzig.irpact.core.message.MessageContent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class CompanyAgentBase extends InformationAgentBase implements CompanyAgent {

    public CompanyAgentBase(
            SimulationEnvironment environment,
            String name,
            double informationAuthority) {
        super(environment, name, informationAuthority);
    }

    @Override
    public Set<Product> getProductPortfolio() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHandling(Agent sender, MessageContent content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(Agent sender, MessageContent content) {
        throw new UnsupportedOperationException();
    }
}
