package de.unileipzig.irpact.jadex.examples.deprecated.old.gui;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.bridge.IExternalAccess;

public class Starter {

    public static void main(String[] args) {

        IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        //IPlatformConfiguration config = PlatformConfigurationHandler.getDefault();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        //config.setLogging(true);
        //config.addComponent("de.unileipzig.irpact.demo.bdi.TestAgentBDI.class");

        IExternalAccess platform = jadex.base.Starter.createPlatform(config)
                .get();
    }
}