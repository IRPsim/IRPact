package de.unileipzig.irpact.core.agent.policy;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.InformationAgentBase;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.simulation.EntityType;
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
    public TaxesScheme getTaxesScheme() {
        return taxesScheme;
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHandling(Agent sender, Message content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(Agent sender, Message content) {
        throw new UnsupportedOperationException();
    }
}
