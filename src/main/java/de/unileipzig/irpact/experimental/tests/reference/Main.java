package de.unileipzig.irpact.experimental.tests.reference;

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

    private static CreationInfo newSender(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".SenderAgent.class");
        info.addArgument("name", name);
        return info;
    }

    private static CreationInfo newBasic(String name, Object x) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".BasicAgent.class");
        info.addArgument("name", name);
        info.addArgument("x", x);
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
        IExternalAccess senderAgent = platform.createComponent(newSender("SENDER"))
                .get();

        RefData<X> r1 = new RefData<>(new X("x"));
        NonRefData<X> r2 = new NonRefData<>(new X("y"));
        System.out.println("> " + r1.hashCode());
        System.out.println("> " + r2.hashCode());
        IExternalAccess basicAgent1 = platform.createComponent(newBasic("BASIC1", r1))
                .get();
        IExternalAccess basicAgent2 = platform.createComponent(newBasic("BASIC2", r2))
                .get();


        log("...agents started");

        ConcurrentUtil.sleepSilently(2000);
        r1.get().data = "a";
        r2.get().data = "b";
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