package de.unileipzig.irpact.experimental.tests.timeModel;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.time.ContinuousTimeModel;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
public class MainContinous {

    private static String getPackageName() {
        return MainContinous.class.getPackage().getName();
    }

    private static CreationInfo newAgentInfo(String name, JadexTimeModel timeModel, long delay) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".TestAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("timeModel", timeModel);
        info.addArgument("delay", delay);
        return info;
    }

    private static void log(String msg) {
        System.out.println("[" + LocalTime.now() + "] [main] " + msg);
    }

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        IClockService clock = JadexUtil2.getClockService(platform);
        ISimulationService simulationService = JadexUtil2.getSimulationService(platform);

        log("change clock");
        simulationService.pause().get();
        simulationService.setClockType(IClock.TYPE_CONTINUOUS).get();
        clock.setDilation(86400.0);
        simulationService.start().get();
        log("new clocktype: " + clock.getClockType());

        log("pause simulation");
        simulationService.pause().get();
        clock.stop();

        ContinuousTimeModel cTimeModel = new ContinuousTimeModel();
        if(true) throw new IllegalStateException();
//        cTimeModel.setClock(clock);
//        cTimeModel.setSimulation(simulationService);
        cTimeModel.setStartYear(2015);
        cTimeModel.setStartTime(clock.getStarttime());


        System.out.println("pre start:   " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        IExternalAccess agent1 = platform.createComponent(newAgentInfo("A_1", cTimeModel, 604800000L)).get();
        IExternalAccess agent2 = platform.createComponent(newAgentInfo("A_2", cTimeModel, 604800000L / 7L)).get();
        System.out.println("post start:  " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());

        log("wait for start");
        ConcurrentUtil.sleepSilently(5000);
        log("start simulation");
        simulationService.start().get();
        clock.start();
        System.out.println("DIL: " + clock.getDilation());

        System.out.println("pre stop:   " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        ConcurrentUtil.sleepSilently(10000);
        System.out.println("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        platform.killComponent().get();
    }
}
