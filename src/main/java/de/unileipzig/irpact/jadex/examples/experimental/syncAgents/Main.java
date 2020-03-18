package de.unileipzig.irpact.jadex.examples.experimental.syncAgents;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class Main {

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        IClockService clockService = platform.searchService(new ServiceQuery<>(IClockService.class))
                .get();
        ISimulationService simulationService = platform.searchService(new ServiceQuery<>(ISimulationService.class))
                .get();

        //geht beides, simu und clock - wichtig ist, dass waitForTick etc genutzt wird, nicht schedule
        simulationService.pause();
        simulationService.setClockType(IClock.TYPE_CONTINUOUS);
        simulationService.pause();
        //clockService.stop();
        //clockService.setClock(IClock.TYPE_CONTINUOUS, platform.searchService(new ServiceQuery<>(IThreadPoolService.class)).get());
        //clockService.stop();

        CreationInfo ci1 = new CreationInfo();
        ci1.setName("Agent1");
        ci1.setFilename("de.unileipzig.irpact.jadex.examples.experimental.syncAgents.TestAgentBDI.class");
        IExternalAccess agent1 = platform.createComponent(ci1)
                .get();

        CreationInfo ci2 = new CreationInfo();
        ci2.setName("Agent2");
        ci2.setFilename("de.unileipzig.irpact.jadex.examples.experimental.syncAgents.TestAgentBDI.class");
        IExternalAccess agent2 = platform.createComponent(ci2)
                .get();

        /*
        System.out.println("suspend");
        platform.getExternalFeature(IExternalExecutionFeature.class)
                .suspendComponent()
                .get();
        */

        Collection<TestService> services = platform.searchServices(new ServiceQuery<>(TestService.class, ServiceScope.PLATFORM))
                .get();
        System.out.println("services: " + services.size());
        System.out.println("prepare");
        ConcurrentUtil.sleepSilently(2000);
        System.out.println("setup");
        for(TestService service: services) {
            service.setup();
        }
        ConcurrentUtil.sleepSilently(2000);
        System.out.println("start");
        //clockService.start();
        simulationService.start();

        ConcurrentUtil.start(5000, platform::killComponent);
    }
}
