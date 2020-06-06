package de.unileipzig.irpact.jadex.examples.deprecated.tests.dummy;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;

/**
 * Startet IRPact.
 * @author Daniel Abitz
 * @since 0.0
 */
public final class StartSimulation {

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        config.setDefaultTimeout(5 * 60 * 1000); //5min
        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        IExternalAccess simulationAgent = platform.createComponent(SimulationAgent.createInfo("SimulationAgent", "SimulationAgent"))
                .get();
    }
}
