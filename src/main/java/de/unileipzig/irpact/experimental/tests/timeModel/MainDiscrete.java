package de.unileipzig.irpact.experimental.tests.timeModel;

import de.unileipzig.irpact.v2.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.v2.jadex.time.DiscreteTimeModel;
import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.v2.jadex.util.JadexUtil2;
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
public class MainDiscrete {

    private static String getPackageName() {
        return MainDiscrete.class.getPackage().getName();
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
        simulationService.setClockType(IClock.TYPE_EVENT_DRIVEN).get();
        clock.setDelta(1L);
        simulationService.start().get();
        log("new clocktype: " + clock.getClockType());

        log("pause simulation");
        simulationService.pause().get();
        clock.stop();

        DiscreteTimeModel dTimeModel = new DiscreteTimeModel();
        dTimeModel.setStartYear(2015, 900000L); //86400000L
        dTimeModel.setClock(clock);
        dTimeModel.setSimulation(simulationService);
        dTimeModel.setStartTick(clock.getTick());


        System.out.println("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        IExternalAccess agent1 = platform.createComponent(newAgentInfo("A_1", dTimeModel, 604800000L)).get();
        IExternalAccess agent2 = platform.createComponent(newAgentInfo("A_2", dTimeModel, 604800000L / 7L)).get();
        System.out.println("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());

        log("wait for start");
        ConcurrentUtil.sleepSilently(5000);
        log("start simulation");
        simulationService.start().get();
        clock.start();

        System.out.println("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        ConcurrentUtil.sleepSilently(10000);
        System.out.println("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        platform.killComponent().get();
    }
}
