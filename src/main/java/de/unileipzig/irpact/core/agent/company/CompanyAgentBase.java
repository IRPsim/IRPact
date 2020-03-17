package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.agent.InformationAgentBase;
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
}
