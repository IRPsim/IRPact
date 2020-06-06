package de.unileipzig.irpact.jadex.start;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.jadex.agent.simulation.JadexKillPlatformAgent;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopSimulation {

    private static final Logger logger = LoggerFactory.getLogger(StopSimulation.class);

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

    @ToDo("logger config wird hier teilweise benoetigt")
    @ToDo("-> wichtige teile einfach selber nochmal aus config auslesen bzw mit dem hauptprog kleine config erstellen")
    public static void main(String[] args) {
        logger.debug("=====START=====");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        PlatformConfigurationHandler.addIntranetComm(config);
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        //config.setLogging(true);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        IExternalAccess simulationAgent = platform.createComponent(createKillAgent("JadexKillPlatformAgent", ""))
                .get();

        logger.debug("[StartPlatform] Agent '{}' created.", simulationAgent.getId().getName());
    }
}
