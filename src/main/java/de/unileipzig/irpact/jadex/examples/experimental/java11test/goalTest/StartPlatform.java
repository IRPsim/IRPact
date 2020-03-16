package de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;

public class StartPlatform {

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        /*
        //internal goals klappen nicht
        CreationInfo ci = new CreationInfo();
        ci.setName("Agent");
        ci.setFilename("de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest.InternalGoalAgentBDI.class");
        */

        //externe goals gehen
        CreationInfo ci = new CreationInfo();
        ci.setName("Agent");
        ci.setFilename("de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest.ExternalGoalAgentBDI.class");

        IExternalAccess agent = platform.createComponent(ci)
                .get();

        ConcurrentUtil.start(5000, platform::killComponent);
    }
}
