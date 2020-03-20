package de.unileipzig.irpact.core.agent.company;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProductIntroductionScheme extends Scheme {

    void handle(CompanyAgent agent, Product product);
}
