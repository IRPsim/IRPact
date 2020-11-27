package de.unileipzig.irpact.experimental.tests.servicesSearch;

import de.unileipzig.irpact.v2.commons.concurrent.ConcurrentUtil;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
public class Main {

    private static String getPackageName() {
        return Main.class.getPackage().getName();
    }

    private static CreationInfo newClient(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".ClientAgent.class");
        info.addArgument("name", name);
        return info;
    }

    private static CreationInfo newScheduler(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".SchedulerAgent.class");
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

        log("start agents...");
        IExternalAccess schedulerAgent = platform.createComponent(newScheduler("SCHEDULER"))
                .get();

        IExternalAccess clientAgent = platform.createComponent(newClient("CLIENT"))
                .get();

        log("...agents started");

        ConcurrentUtil.sleepSilently(5000);
        platform.killComponent()
                .get();
    }
}

/*
@Reference(local = true, remote = true)
    - immer setzen, sonst copy!!!
    - funktioniert auch fuer Services (also deren Interface)
 */