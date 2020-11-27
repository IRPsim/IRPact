package de.unileipzig.irpact.experimental.tests.extendedagent;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
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

    private static CreationInfo newHello1(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".HelloAgent.class");
        info.addArgument("name", name);
        return info;
    }

    private static CreationInfo newHello2(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".HelloAgent2.class");
        info.addArgument("name", name);
        return info;
    }

    private static CreationInfo newHello3(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".HelloAgent3.class");
        info.addArgument("name", name);
        return info;
    }

    private static CreationInfo newHello4(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".HelloAgent4.class");
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
        IExternalAccess a1 = platform.createComponent(newHello1("Hello1"))
                .get();
        IExternalAccess a2 = platform.createComponent(newHello2("Hello2"))
                .get();
        IExternalAccess a3 = platform.createComponent(newHello3("Hello3"))
                .get();
        IExternalAccess a4 = platform.createComponent(newHello4("Hello4"))
                .get();

        ConcurrentUtil.sleepSilently(5000);
        platform.killComponent()
                .get();
    }
}

/*
extend: funktioniert
implements: funktioniert nicht
lifecycle geht auch
 */