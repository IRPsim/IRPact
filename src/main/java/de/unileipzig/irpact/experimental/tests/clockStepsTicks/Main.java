package de.unileipzig.irpact.experimental.tests.clockStepsTicks;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.experimental.ExperimentalUtil;
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

    private static CreationInfo newClockAgent() {
        CreationInfo info = new CreationInfo();
        info.setName("clock");
        info.setFilename(getPackageName() + ".ClockAgent.class");
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

        IClockService clock = ExperimentalUtil.getClockService(platform);
        ISimulationService simulationService = ExperimentalUtil.getSimulationService(platform);

        clock.setDelta(1000);

        log("change clock type");
        log("old clocktype: " + clock.getClockType());
        simulationService.pause().get();
        simulationService.setClockType(IClock.TYPE_EVENT_DRIVEN);
        simulationService.start().get();
        log("new clocktype: " + clock.getClockType());

        log("delta: " + clock.getDelta());
        log("dilation: " + clock.getDilation());

        simulationService.pause().get();

        log("2 sek pause");
        ConcurrentUtil.sleepSilently(1000);
        IExternalAccess clockAgent = platform.createComponent(newClockAgent())
                .get();
        ConcurrentUtil.sleepSilently(1000);
        log("start agents...");

        IExternalAccess agent0 = platform.createComponent(newAgentInfo("agent#0"))
                .get();
        IExternalAccess agent1 = platform.createComponent(newAgentInfo("agent#1"))
                .get();
        IExternalAccess agent2 = platform.createComponent(newAgentInfo("agent#2"))
                .get();


        simulationService.start().get();

        log("...agents started");

        log("current tick: " + clock.getTick());
        ConcurrentUtil.sleepSilently(5000);
        log("current tick: " + clock.getTick());
        ConcurrentUtil.sleepSilently(5000);
        log("current tick: " + clock.getTick());
        platform.killComponent()
                .get();
    }
}

/*
Clock-Infos:
    https://download.actoron.com/docs/nightlies/latest/jadex-mkdocs/tools/09%20Simulation%20Control/

Erkentnisse:
        delta:
            - rechnet ms in ticks um, auf Basis von Millisekunden
            - Beispiel:  100 -> 1 sek -> 10 ms -> 10 ticks je Sekunde
            - Beispiel: 1000 -> 1 sek -> 1 ms -> 1 tick je Sekunde

        simulationService.pause();
            - onInit und onStart werden ganz normal aufgerufen
            - schedule-Tasks werden NICHT aufgerufen
            - killComponent() bei Platform funktioniert weiterhin

        IClock.TYPE_EVENT_DRIVEN
            - execFeature.waitForDelay(time) nicht mehr richtig nutzbar?


Sucht nach Agent:
        reqFeature.searchServices(new ServiceQuery<>(ClockAgentService.class, ServiceScope.NETWORK))
                .addResultListener(new IIntermediateResultListener<ClockAgentService>() {
                    @Override
                    public void intermediateResultAvailable(ClockAgentService result) {
                        System.out.println("FOUND: " + result);
                    }

                    @Override
                    public void finished() {
                    }

                    @Override
                    public void exceptionOccurred(Exception exception) {
                    }

                    @Override
                    public void resultAvailable(Collection<ClockAgentService> result) {
                    }
                });

vs.

        clockAgent = reqFeature.searchServices(new ServiceQuery<>(ClockAgentService.class, ServiceScope.NETWORK))
                .get()
 */