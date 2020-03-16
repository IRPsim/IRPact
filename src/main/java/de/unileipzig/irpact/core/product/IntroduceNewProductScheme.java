package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface IntroduceNewProductScheme extends Scheme {

    Collection<Product> introduceNewProducts(CompanyAgent agent);
}
