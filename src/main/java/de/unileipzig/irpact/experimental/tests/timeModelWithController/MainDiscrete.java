package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.JadexLifeCycleControl;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;
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
public class MainDiscrete {

    private static String getPackageName() {
        return MainDiscrete.class.getPackage().getName();
    }

    private static CreationInfo newModelInfo(String name, JadexTimeModel timeModel, JadexLifeCycleControl simulationControl) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".TimeModelAgent.class");
        info.addArgument("name", name);
        info.addArgument("timeModel", timeModel);
        info.addArgument("simulationControl", simulationControl);
        return info;
    }

    private static CreationInfo newAgentInfo(String name, long delay) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".TestAgentBDI.class");
        info.addArgument("name", name);
        info.addArgument("delay", delay);
        return info;
    }

    private static void log(String msg) {
        System.out.println("[" + LocalTime.now() + "] [main] " + msg);
    }

    public static void main(String[] args) throws InterruptedException {
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
        if(true) throw new IllegalStateException();
//        dTimeModel.setClock(clock);
//        dTimeModel.setSimulation(simulationService);
        dTimeModel.setStartTick(clock.getTick());
        dTimeModel.setEndTime(dTimeModel.plusYears(dTimeModel.startTime(), 1L));
        log("start: " + dTimeModel.startTime());
        log("end:   " + dTimeModel.endTime());

        log("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        BasicJadexLifeCycleControl simulationControl = new BasicJadexLifeCycleControl();
        simulationControl.setTotalNumberOfAgents(4);

        IExternalAccess modelAgent = platform.createComponent(newModelInfo("M", dTimeModel, simulationControl)).get();
        IExternalAccess agent1 = platform.createComponent(newAgentInfo("A_1", 604800000L)).get();
        IExternalAccess agent2 = platform.createComponent(newAgentInfo("A_2", 604800000L / 7L)).get();
        IExternalAccess agent3 = platform.createComponent(newAgentInfo("A_3", 0L)).get();
        log("post stop:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());

        log("wait for start");
        //ConcurrentUtil.sleepSilently(5000);
        simulationControl.waitForCreationFinished();
        log("start simulation");
        simulationService.start().get();
        clock.start();

        //log("pre sleep:   " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        //ConcurrentUtil.sleepSilently(10000);
        //log("post sleep:  " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        //platform.killComponent().get();
        log("pre termi:   " +  clock.getTick() + " " + clock.getTime() + " " + dTimeModel.now());
        platform.waitForTermination().get();
        log("post termi:  ..."); //clock is null
    }
}
