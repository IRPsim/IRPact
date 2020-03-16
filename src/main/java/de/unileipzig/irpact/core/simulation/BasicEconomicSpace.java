package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.policy.TaxesScheme;

/**
 * @author Daniel Abitz
 */
public class BasicEconomicSpace implements EconomicSpace {

    private TaxesScheme taxesScheme;

    public BasicEconomicSpace(TaxesScheme taxesScheme) {
        setTaxesScheme(taxesScheme);
    }

    @Override
    public void setTaxesScheme(TaxesScheme taxesScheme) {
        this.taxesScheme = Check.requireNonNull(taxesScheme, "taxesScheme");
    }

    @Override
    public TaxesScheme getTaxesScheme() {
        return taxesScheme;
    }
}
