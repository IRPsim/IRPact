package de.unileipzig.irpact.core.agent.policy;

import de.unileipzig.irpact.core.agent.InformationAgent;

/**
 * @author Daniel Abitz
 */
public interface PolicyAgent extends InformationAgent {

    @Override
    PolicyAgentIdentifier getIdentifier();

    TaxesScheme getTaxesScheme();
}
