package de.unileipzig.irpact.experimental.tests.stepWithDelay.timeDriven;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.execution.IExecutionService;
import jadex.bridge.service.types.simulation.ISimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Daniel Abitz
 */
public class Main {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static String getPackageName() {
        return Main.class.getPackage().getName();
    }

    private static CreationInfo newAgent(String name, CountDownLatch latch) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".TestAgentBDI.class");
        info.addArgument("NAME", name);
        info.addArgument("LATCH", latch);
        return info;
    }

    public static void main(String[] args) throws InterruptedException {
        Logback.setupSystemOutAndErr();
        LOGGER.trace("TEST");

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        ISimulationService simulationService = JadexUtil2.getSimulationService(platform);
        IClockService clock = simulationService.getClockService();
        IExecutionService exec = simulationService.getExecutorService();

        LOGGER.trace("change clock");
        System.out.println("exec? " + simulationService.isExecuting().get());
        simulationService.pause().get();
        System.out.println("exec? " + simulationService.isExecuting().get());
        simulationService.setClockType(IClock.TYPE_EVENT_DRIVEN).get();
        clock.setDelta(1L);
        simulationService.start().get();
        LOGGER.trace("new clocktype: " + clock.getClockType());

        LOGGER.trace("pause simulation");
        simulationService.pause().get();
        clock.stop();

        LOGGER.trace("start agents...");
        final int agents = 3;
        CountDownLatch latch = new CountDownLatch(agents);
        for(int i = 0; i < agents; i++) {
            IExternalAccess senderAgent = platform.createComponent(newAgent("Agent#" + i, latch))
                    .get();
        }

        LOGGER.trace("...wait for agents");
        latch.await();
        LOGGER.trace("...agents started");

        LOGGER.trace("start simulation");
        clock.start();
        simulationService.start().get();

//        for(int i = 0; i < 10; i++) {
//            simulationService.stepTime();
//            boolean isExec = simulationService.isExecuting().get();
//            System.out.println("isExec? " + isExec);
//            if(isExec) {
//                exec.getNextIdleFuture().get();
//            }
//        }

//        simulationService.start().get();
//        ConcurrentUtil.sleepSilently(1000);
//        LOGGER.trace("tick: {}", clock.getTick());
//        for(int i = 0; i < 10; i++) {
//            platform.waitForTick().get();
//            LOGGER.trace("tick: {}", clock.getTick());
//        }

        ConcurrentUtil.sleepSilently(10000);
        LOGGER.trace("kill all");
        platform.killComponent()
                .get();
    }
}

/*
@Reference(local = true, remote = true)
    - immer setzen, sonst copy!!!
    - funktioniert auch fuer Services (also deren Interface)
 */