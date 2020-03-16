package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.policy.TaxesScheme;

/**
 * @author Daniel Abitz
 */
public interface EconomicSpace {

    void setTaxesScheme(TaxesScheme taxesScheme);

    TaxesScheme getTaxesScheme();
}
