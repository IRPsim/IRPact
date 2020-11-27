package de.unileipzig.irpact.experimental.tests.syncStart;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
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
public class Main {

    private static String getPackageName() {
        return Main.class.getPackage().getName();
    }

    private static CreationInfo newAgentInfo(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".TestAgentBDI.class");
        info.addArgument("name", name);
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
        simulationService.setClockType(IClock.TYPE_CONTINUOUS);
        simulationService.start().get();
        log("new clocktype: " + clock.getClockType());

        log("pause simulation");
        simulationService.pause().get();
        clock.stop();

        for(int i = 0; i < 4; i++) {
            IExternalAccess agent = platform.createComponent(newAgentInfo("MyAgent#" + i)).get();
        }

        log("wait for start");
        ConcurrentUtil.sleepSilently(5000);
        log("start simulation");
        simulationService.start().get();
        clock.start();

        ConcurrentUtil.sleepSilently(5000);
        platform.killComponent().get();
    }
}
