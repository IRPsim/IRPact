package de.unileipzig.irpact.jadex.examples.tests.killtest;

import de.unileipzig.irpact.jadex.agent.simulation.JadexKillPlatformAgent;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KillPlatform {

    private static final Logger logger = LoggerFactory.getLogger(KillPlatform.class);

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createKillAgent(
            String name,
            String platformName) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.agent.simulation.JadexKillPlatformAgent.class");
        info.addArgument(JadexKillPlatformAgent.NAME, name);
        info.addArgument(JadexKillPlatformAgent.PLATFORM_TO_KILL, platformName);
        return info;
    }

    public static void main(String[] args) {
        logger.debug("=====START=====");

        IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        //PlatformConfigurationHandler.addIntranetComm(config);
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(5 * 60 * 1000);
        //config.setLogging(true);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        IExternalAccess simulationAgent = platform.createComponent(createKillAgent("JadexKillPlatformAgent", ""))
                .get();

        logger.debug("[StartPlatform] Agent '{}' created.", simulationAgent.getId().getName());
    }
}
