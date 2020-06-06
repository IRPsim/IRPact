package de.unileipzig.irpact.jadex.examples.experimental.java11test.planTest;

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

        CreationInfo ci = new CreationInfo();
        ci.setName("Agent");
        ci.setFilename("de.unileipzig.irpact.jadex.examples.experimental.java11test.planTest.ExternalPlanAgentBDI.class");

        /*
        //bei internen klassen kommt ein fehler
        CreationInfo ci = new CreationInfo();
        ci.setName("Agent");
        ci.setFilename("de.unileipzig.irpact.jadex.examples.experimental.java11test.planTest.InternalPlanAgentBDI.class");
        */

        IExternalAccess agent = platform.createComponent(ci)
                .get();

        ConcurrentUtil.start(5000, platform::killComponent);
    }
}
