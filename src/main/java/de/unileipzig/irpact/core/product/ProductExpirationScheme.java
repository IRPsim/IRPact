package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;

import java.util.Collection;

/**
 * Beschreibt was passiert, wenn ein Produkt entfaellt.
 * @author Daniel Abitz
 */
public interface ProductExpirationScheme extends Scheme {

    void expire(CompanyAgent agent, Product product);
}
