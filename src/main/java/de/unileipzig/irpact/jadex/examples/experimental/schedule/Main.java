package de.unileipzig.irpact.jadex.examples.experimental.schedule;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;

/**
 * @author Daniel Abitz
 */
public class Main {

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        CreationInfo ci1 = new CreationInfo();
        ci1.setName("Agent1");
        ci1.setFilename("de.unileipzig.irpact.jadex.examples.experimental.schedule.TestAgentBDI.class");
        IExternalAccess agent1 = platform.createComponent(ci1)
                .get();

        ConcurrentUtil.start(5000, platform::killComponent);
    }
}
