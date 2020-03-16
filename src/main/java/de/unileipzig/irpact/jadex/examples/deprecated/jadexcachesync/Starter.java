package de.unileipzig.irpact.jadex.examples.deprecated.jadexcachesync;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IExternalExecutionFeature;
import jadex.bridge.service.types.cms.CreationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starter {

    private static final Logger logger = LoggerFactory.getLogger("JadexLogger");

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createConsumerAgentBDIInfo(
            String name,
            Logger logger,
            JadexSimulationEnvironment env) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.jadexcachesync.ConsumerAgentBDI.class");
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
        info.setFilename("de.unileipzig.irpact.jadex.examples.jadexcachesync.SimuAgentBDI.class");
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
        IExternalAccess agent0 = platform.createComponent(createConsumerAgentBDIInfo("agent0", logger, env))
                .get();
        IExternalAccess agent1 = platform.createComponent(createConsumerAgentBDIInfo("agent1", logger, env))
                .get();
        IExternalAccess agent2 = platform.createComponent(createConsumerAgentBDIInfo("agent2", logger, env))
                .get();

        /*
        Grundidee: Alle Komponenten starten im suspend Modus, damit keine voreilige Datengeschichte passiert.
        Sobald Alle Agenten erstellt wurden, kann via platform.resume alles gestartet werden.
         */
        //FUNZT !!!! SEEEEEHR GUT !
        ConcurrentUtil.sleepSilently(3000);
        IExternalExecutionFeature platformExec = platform.getExternalFeature(IExternalExecutionFeature.class);
        //platformExec.suspendComponent();
        ConcurrentUtil.sleepSilently(10000);
        platformExec.resumeComponent();

        /*
        ConcurrentUtil.sleepSilently(5000);
        Collection<ExtAccess> persiColl = platform.searchServices(JadexUtil.searchPlatform(ExtAccess.class))
                .get();
        ExtAccess acc = CollectionUtil.getFirst(persiColl);
        logger.debug("WO BIN ICH @Starter: {}", Thread.currentThread().getName());
        acc.doIt();
        */

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
