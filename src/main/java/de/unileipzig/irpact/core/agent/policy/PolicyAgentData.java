package de.unileipzig.irpact.core.agent.policy;

import de.unileipzig.irpact.core.agent.InformationAgentData;

/**
 * @author Daniel Abitz
 */
public class PolicyAgentData extends InformationAgentData {

    public PolicyAgentData() {
    }

    protected TaxesScheme taxesScheme;
    public TaxesScheme getTaxesScheme() {
        return taxesScheme;
    }
    public void setTaxesScheme(TaxesScheme taxesScheme) {
        this.taxesScheme = taxesScheme;
    }
}
