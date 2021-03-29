package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;

/**
 * @author Daniel Abitz
 */
public abstract class AgentBase extends SimulationEntityBase implements Agent {

    protected int maxNumberOfActions;

    public int getMaxNumberOfActions() {
        return maxNumberOfActions;
    }

    public void setMaxNumberOfActions(int maxNumberOfActions) {
        this.maxNumberOfActions = maxNumberOfActions;
    }
}
