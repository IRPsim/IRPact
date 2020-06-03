package de.unileipzig.irpact.jadex.examples.deprecated.tests.starttest;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.OLD.io.config.JadexConfiguration;
import de.unileipzig.irpact.OLD.io.config.JadexConfigurationBuilder;
import de.unileipzig.irpact.jadex.start.StartSimulation;

public class StartSimulationTest {

    public static void main(String[] args) {
        JadexConfiguration config = new JadexConfigurationBuilder().initMinimal()
                .build();
        StartSimulation.start(config);
        ConcurrentUtil.start(0, () -> {
            config.getEnvironment().getLogger().info("10 sek");
            ConcurrentUtil.sleepSilently(5000);
            config.getEnvironment().getLogger().info("5 sek");
            ConcurrentUtil.sleepSilently(5000);
            config.getEnvironment().getLogger().info("kill");
            config.getEnvironment().getPlatform().killComponent();
        });
    }
}
