package de.unileipzig.irpact.jadex.examples.deprecated.twoapps;

import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;

/**
 * @author Daniel Abitz
 */
public class Start1 {

    public static void main(String[] args) {
        //IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        //config.setDefaultTimeout(5 * 60 * 1000); //5min
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        PlatformConfigurationHandler.addIntranetComm(config);
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setAwareness(true);
        config.setDefaultTimeout(-1L);
        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        CreationInfo ci = new CreationInfo();
        ci.setName("Agent0");
        ci.setFilename("de.unileipzig.irpact.jadex.examples.twoapps.Agent1Agent.class");

        IExternalAccess access = platform.createComponent(ci)
                .get();
    }
}
