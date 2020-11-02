package de.unileipzig.irpact.v2.jadex.agents.simulation;

import de.unileipzig.irpact.v2.core.misc.InitializationData;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexSimulationAgentInitializationData implements InitializationData {

    protected String name;
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    protected JadexSimulationEnvironment environment;
    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }
    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }
}
