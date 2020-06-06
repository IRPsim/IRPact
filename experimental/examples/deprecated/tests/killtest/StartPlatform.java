package de.unileipzig.irpact.jadex.examples.deprecated.tests.killtest;

import de.unileipzig.irpact.jadex.agent.simulation.JadexSimulationControlAgent;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartPlatform {

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createSimulationControlAgent(
            String name,
            JadexSimulationEnvironment environment) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.agent.simulation.JadexSimulationControlAgent.class");
        info.addArgument(JadexSimulationControlAgent.NAME, name);
        info.addArgument(JadexSimulationControlAgent.ENVIRONMENT, environment);
        return info;
    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(StartPlatform.class);
        logger.debug("=====START=====");

        IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        //PlatformConfigurationHandler.addIntranetComm(config);
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(5 * 60 * 1000);
        //config.setLogging(true);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        BasicJadexSimulationEnvironment environment = new BasicJadexSimulationEnvironment();
        environment.setLogger(logger);
        environment.setPlatform(platform);

        IExternalAccess simulationAgent = platform.createComponent(createSimulationControlAgent("JadexSimulationControlAgent", environment))
                .get();

        logger.debug("[StartPlatform] Agent '{}' created.", simulationAgent.getId().getName());
    }
}
