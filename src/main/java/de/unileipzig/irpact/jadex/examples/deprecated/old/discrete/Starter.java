package de.unileipzig.irpact.jadex.examples.deprecated.old.discrete;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;
import jadex.commons.future.IResultListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starter {

    private static final Logger logger = LoggerFactory.getLogger("JadexLogger");

    @SuppressWarnings("SameParameterValue")
    private static CreationInfo createConsumerAgentBDIInfo(
            String name,
            Logger logger) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename("de.unileipzig.irpact.jadex.examples.old.discrete.ConsumerAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("logger", logger);
        return info;
    }

    public static void main(String[] args) {
        logger.debug("=====START=====");

        //IPlatformConfiguration config = PlatformConfigurationHandler.getDefaultNoGui();
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        //config.setLogging(true);
        //config.addComponent("de.unileipzig.irpact.demo.bdi.TestAgentBDI.class");
        //config.getExtendedPlatformConfiguration().setSimul(true);

        IExternalAccess platform = jadex.base.Starter.createPlatform(config)
                .get();


        IExecutionFeature execFeature = platform.getExternalFeature(IExecutionFeature.class);
        execFeature.createResultListener(new IResultListener<Object>() {

            @Override
            public void resultAvailable(Object result) {
                logger.info("EVENT {}", result);
            }

            @Override
            public void exceptionOccurred(Exception exception) {
                logger.error("ERROR", exception);
            }
        });

        IClockService clockService = platform.searchService(new ServiceQuery<>(IClockService.class)).get();
        IThreadPoolService threadPoolService = platform.searchService(new ServiceQuery<>(IThreadPoolService.class)).get();
        Object threadPool = Ref.getValue(threadPoolService, "threadpool");

        //System.out.println(threadPoolService.getClass());
        //System.out.println(threadPool.getClass());

        //JavaThreadPool jtp = new JavaThreadPool();

        clockService.setClock(IClock.TYPE_EVENT_DRIVEN, threadPoolService);
        System.out.println(clockService.getClockType());

        ISimulationService simulationService = platform.searchService(new ServiceQuery<>(ISimulationService.class)).get();
        simulationService.pause();
        //clockService.setDilation(2.0);

        //simulationService.pause().get();
        /*
        simulationService.setClockType(IClock.TYPE_EVENT_DRIVEN);
        simulationService.start().get();
        simulationService.pause().get();
        */

        IExternalAccess agent0 = platform.createComponent(createConsumerAgentBDIInfo("agent0", logger))
                .get();

        IExternalAccess agent1 = platform.createComponent(createConsumerAgentBDIInfo("agent1", logger))
                .get();

        ConcurrentUtil.start(0L, () -> {

            logger.debug("kill in 20");
            logger.info("CLOCK: {} {}", clockService.getTime(), clockService.getTick());
            simulationService.stepTime().get();
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 15");
            logger.info("CLOCK: {} {}", clockService.getTime(), clockService.getTick());
            simulationService.stepTime().get();
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 10");
            logger.info("CLOCK: {} {}", clockService.getTime(), clockService.getTick());
            simulationService.stepTime().get();
            ConcurrentUtil.sleepSilently(5000);
            logger.debug("kill in 5");
            logger.info("CLOCK: {} {}", clockService.getTime(), clockService.getTick());
            ConcurrentUtil.sleepSilently(5000);

            platform.killComponent();
            logger.debug("=====END=====");
        });
    }
}
