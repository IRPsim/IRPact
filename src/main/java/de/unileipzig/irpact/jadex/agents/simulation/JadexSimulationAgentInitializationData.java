package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.core.misc.InitializationData;
import de.unileipzig.irpact.jadex.simulation.JadexLiveCycleControl;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexSimulationAgentInitializationData implements InitializationData {

    public JadexSimulationAgentInitializationData() {
    }

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

    protected JadexLiveCycleControl control;
    public void setControl(JadexLiveCycleControl control) {
        this.control = control;
    }
    public JadexLiveCycleControl getControl() {
        return control;
    }
}
