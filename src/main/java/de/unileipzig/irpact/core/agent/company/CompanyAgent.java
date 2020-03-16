package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.agent.InformationAgent;
import de.unileipzig.irpact.core.product.Product;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface CompanyAgent extends InformationAgent {

    @Override
    CompanyAgentIdentifier getIdentifier();

    Set<Product> getProductPortfolio();
}
