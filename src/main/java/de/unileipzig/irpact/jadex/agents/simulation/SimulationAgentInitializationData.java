package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public final class SimulationAgentInitializationData {

    public SimulationAgentInitializationData() {
    }

    protected String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    protected JadexSimulationEnvironment environment;
    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }
    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }
}
