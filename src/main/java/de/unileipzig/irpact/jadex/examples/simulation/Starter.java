package de.unileipzig.irpact.jadex.examples.simulation;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.persistence.IPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Starter {

    private static final Logger logger = LoggerFactory.getLogger("JadexLogger");

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createConsumerAgentBDIInfo(
            String name,
            Logger logger,
            JadexSimulationEnvironment env) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.simulation.ConsumerAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("env", env);
        return info;
    }

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createSimuAgentBDIInfo(
            String name,
            Logger logger,
            JadexSimulationEnvironment env) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.simulation.SimuAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        info.addArgument("env", env);
        return info;
    }

    public static void main(String[] args) {
        logger.debug("=====START=====");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        //config.setLogging(true);


        IExternalAccess platform = jadex.base.Starter.createPlatform(config)
                .get();

        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.setPlatform(platform);

        IExternalAccess simu0 = platform.createComponent(createSimuAgentBDIInfo("simu0", logger, env))
                .get();

        ConcurrentUtil.sleepSilently(500);

        IExternalAccess agent0 = platform.createComponent(createConsumerAgentBDIInfo("agent0", logger, env))
                .get();
        /*
        IExternalAccess agent1 = platform.createComponent(createConsumerAgentBDIInfo("agent1", logger, env))
                .get();
        IExternalAccess agent2 = platform.createComponent(createConsumerAgentBDIInfo("agent2", logger, env))
                .get();
        */

        Collection<IPersistenceService> persiColl = platform.searchServices(JadexUtil.searchPlatform(IPersistenceService.class))
                .get();
        System.out.println(persiColl.size());

        ConcurrentUtil.start(0L, () -> {

            logger.debug("kill in 20");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 15");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 10");
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 5");
            ConcurrentUtil.sleepSilently(5000);

            platform.killComponent();
            logger.debug("=====END=====");
        });
    }
}
