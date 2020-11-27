package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.v2.jadex.simulation.BasicJadexSimulationControl;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationControl;
import de.unileipzig.irpact.v2.jadex.time.ContinuousTimeModel;
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
public class MainContinous {

    private static String getPackageName() {
        return MainContinous.class.getPackage().getName();
    }

    private static CreationInfo newModelInfo(String name, JadexTimeModel timeModel, JadexSimulationControl simulationControl) {
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
        simulationService.setClockType(IClock.TYPE_CONTINUOUS).get();
        clock.setDilation(86400.0 * 7.0);
        simulationService.start().get();
        log("new clocktype: " + clock.getClockType());

        log("pause simulation");
        simulationService.pause().get();
        clock.stop();

        ContinuousTimeModel cTimeModel = new ContinuousTimeModel();
        cTimeModel.setClock(clock);
        cTimeModel.setSimulation(simulationService);
        cTimeModel.setStartYear(2015);
        cTimeModel.setStartTime(clock.getStarttime());
        cTimeModel.setEnd(cTimeModel.plusYears(cTimeModel.getStart(), 1L));
        log("start: " + cTimeModel.getStart());
        log("end:   " + cTimeModel.getEnd());

        log("pre start:   " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        BasicJadexSimulationControl simulationControl = new BasicJadexSimulationControl();
        simulationControl.setNumberOfAgents(4);

        IExternalAccess modelAgent = platform.createComponent(newModelInfo("M", cTimeModel, simulationControl)).get();
        IExternalAccess agent1 = platform.createComponent(newAgentInfo("A_1", 604800000L)).get();
        IExternalAccess agent2 = platform.createComponent(newAgentInfo("A_2", 604800000L / 7L)).get();
        IExternalAccess agent3 = platform.createComponent(newAgentInfo("A_3", 0L)).get();
        log("post start:  " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());

        log("wait for start");
//        ConcurrentUtil.sleepSilently(5000);
        simulationControl.waitForCreationFinished();
        log("start simulation");
        simulationService.start().get();
        clock.start();
        log("DILATION: " + clock.getDilation());

        //log("pre sleep:   " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        //ConcurrentUtil.sleepSilently(10000);
        //log("post sleep:  " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        //platform.killComponent().get();
        log("pre termi:   " +  clock.getTick() + " " + clock.getTime() + " " + cTimeModel.now());
        platform.waitForTermination().get();
        log("post termi:  ..."); //clock is null
    }
}
