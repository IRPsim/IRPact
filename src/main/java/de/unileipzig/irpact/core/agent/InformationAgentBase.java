package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public abstract class InformationAgentBase extends AgentBase implements InformationAgent {

    protected double informationAuthority;

    public InformationAgentBase() {
    }

    public InformationAgentBase(SimulationEnvironment environment, String name, double informationAuthority) {
        super(environment, name);
        this.informationAuthority = informationAuthority;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }
}
