package de.unileipzig.irpact.jadex.examples.experimental.threadtest;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.util.IRPactThreadPool;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import jadex.bridge.service.types.threadpool.IThreadPoolService;

import java.util.Map;

public class StartPlatform {

    public static void main(String[] args) {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        ISimulationService simulationService = platform.searchService(new ServiceQuery<>(ISimulationService.class)).get();
        IClockService clockService = platform.searchService(new ServiceQuery<>(IClockService.class)).get();
        ////momentaner service
        IThreadPoolService threadPoolService = platform.searchService(new ServiceQuery<>(IThreadPoolService.class)).get();
        ////custom service
        //IRPactThreadPool threadPoolService = new IRPactThreadPool(true, 5, 20, 60, TimeUnit.SECONDS);

        simulationService.pause();
        clockService.setClock(IClock.TYPE_CONTINUOUS, threadPoolService);
        simulationService.start();

        //overflow?
        /*
        CreationInfo[] infos = new CreationInfo[1000];
        for(int i = 0; i < infos.length; i++) {
            CreationInfo ci = new CreationInfo();
            ci.setName("Agent#" + i);
            ci.setFilename("de.unileipzig.irpact.jadex.examples.experimental.threadtest.TestAgent.class");
            infos[i] = ci;
        }
        Collection<IExternalAccess> agents = platform.createComponents(infos)
                .get();
        */
        int tCount0 = Thread.activeCount();
        for(int i = 0; i < 20000; i++) {
            CreationInfo ci = new CreationInfo();
            ci.setName("Agent#" + i);
            ci.setFilename("de.unileipzig.irpact.jadex.examples.experimental.threadtest.TestAgent.class");
            IExternalAccess agent = platform.createComponent(ci)
                    .get();
        }
        int tCount1 = Thread.activeCount();

        //ConcurrentUtil.start(5000, platform::killComponent);
        ConcurrentUtil.sleepSilently(3000);
        platform.killComponent().get();

        //System.out.println("agents: " + agents.size());
        int sum = 0;
        for(Map.Entry<String, Integer> entry: TestAgent.counter.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            sum += entry.getValue();
        }
        System.out.println("-> sum: " + sum);
        int tCount2 = Thread.activeCount();
        System.out.println("-> tCount0: " + tCount0);
        System.out.println("-> tCount1: " + tCount1);
        System.out.println("-> tCount2: " + tCount2);
    }
}
