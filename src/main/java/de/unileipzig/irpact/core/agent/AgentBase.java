package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public abstract class AgentBase extends SimulationEntityBase implements Agent {

    public AgentBase() {
    }

    public AgentBase(SimulationEnvironment environment, String name) {
        super(environment, name);
    }
}
