package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

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
@SuppressWarnings("SameParameterValue")
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

    private static CreationInfo newReceiver(String name) {
        CreationInfo info = new CreationInfo();
        info.setName(name);
        info.setFilename(getPackageName() + ".ReceiverAgent.class");
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
        IExternalAccess receiverAgent = platform.createComponent(newReceiver("RECEIVER"))
                .get();

        ConcurrentUtil.sleepSilently(500);

        IExternalAccess senderAgent0 = platform.createComponent(newSender("SENDER#0"))
                .get();
        IExternalAccess senderAgent1 = platform.createComponent(newSender("SENDER#1"))
                .get();

        log("...agents started");

        ConcurrentUtil.sleepSilently(6000);
        platform.killComponent()
                .get();
    }
}

/*
@Reference(local = true, remote = true)
    - funktioniert fuer interface und klassen
    - immer setzen, sonst copy!!!
    - funktioniert auch fuer Services (also deren Interface)
    - falls "false", dann NullPointer -> TODO: gucken wie die Klasse aufgebaut sein muss, damit sie rekonstruiert werden kann
    - beachtet nur die hauptklasse - enthaltende klassen (siehe DeepData) werden als Referenz genutzt


TODO:
    - Datenaustausch im Step/Event-System untersuchen
 */