package de.unileipzig.irpact.core.agent.policy;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.InformationAgentBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class PolicyAgentBase extends InformationAgentBase implements PolicyAgent {

    protected TaxesScheme taxesScheme;

    public PolicyAgentBase(
            SimulationEnvironment environment,
            String name,
            double informationAuthority,
            TaxesScheme taxesScheme) {
        super(environment, name, informationAuthority);
        this.taxesScheme = Check.requireNonNull(taxesScheme, "taxesScheme");
    }

    @Override
    public PolicyAgentIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TaxesScheme getTaxesScheme() {
        return taxesScheme;
    }
}
