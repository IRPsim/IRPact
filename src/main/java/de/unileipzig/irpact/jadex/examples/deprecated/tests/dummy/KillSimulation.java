package de.unileipzig.irpact.jadex.examples.deprecated.tests.dummy;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;

/**
 * Beendet IRPact.
 * @author Daniel Abitz
 * @since 0.0
 */
public final class KillSimulation {

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        IExternalAccess killAgent = platform.createComponent(KillAgent.createInfo("KillAgent", "KillAgent"))
                .get();
    }
}
