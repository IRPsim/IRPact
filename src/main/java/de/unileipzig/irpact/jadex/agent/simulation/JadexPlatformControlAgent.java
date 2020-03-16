package de.unileipzig.irpact.jadex.agent.simulation;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import jadex.bridge.IExternalAccess;
import jadex.commons.future.IFuture;
import org.slf4j.Logger;

import java.util.Map;

public abstract class JadexPlatformControlAgent extends JadexAgentBase {

    protected abstract IExternalAccess getPlatform();

    protected abstract Logger logger();

    protected String getPlatformName() {
        return getPlatform().getId().getPlatformName();
    }

    protected void killThisPlatformAsync() {
        Thread t = new Thread(() -> {
            ConcurrentUtil.sleepSilently(100); //sonst Fehler?
            killThisPlatform();
        }, "KillPlatformThread");
        t.start();
    }

    protected void killThisPlatform() {
        logger().info("[{}] Kill Platform '{}'.", getName(), getPlatformName());
        IExternalAccess platform = getPlatform();
        IFuture<Map<String, Object>> future = platform.killComponent();
        Map<String, Object> result = future.get();
        logger().info("[{}] Finished: {}", getName(), result != null);
    }
}
