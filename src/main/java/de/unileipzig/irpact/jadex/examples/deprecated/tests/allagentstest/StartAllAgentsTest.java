package de.unileipzig.irpact.jadex.examples.deprecated.tests.allagentstest;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.OLD.io.config.JadexConfiguration;
import de.unileipzig.irpact.OLD.io.config.JadexConfigurationBuilder;
import de.unileipzig.irpact.OLD.io.config.JadexLogConfig;
import de.unileipzig.irpact.jadex.examples.deprecated.tests.configs.AllAgentsAnd2Groups;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;

/**
 * @author Daniel Abitz
 */
public class StartAllAgentsTest {

    public static void main(String[] args) {
        JadexLogConfig logConfig = new JadexLogConfig(
                "",
                true,
                true,
                true,
                false,
                true,
                false
        );
        JadexConfigurationBuilder cb = AllAgentsAnd2Groups.builder()
                .setLogConfig(logConfig);
        JadexConfiguration config = cb.validate().build();
        StartSimulation.start(config);
        ConcurrentUtil.start(0, () -> {
            config.getEnvironment().getLogger().info("20 sek");
            ConcurrentUtil.sleepSilently(5000);
            config.getEnvironment().getLogger().info("15 sek");
            ConcurrentUtil.sleepSilently(5000);
            config.getEnvironment().getLogger().info("10 sek");
            ConcurrentUtil.sleepSilently(5000);
            config.getEnvironment().getLogger().info("5 sek");
            ConcurrentUtil.sleepSilently(5000);
            config.getEnvironment().getLogger().info("kill");
            ((JadexSimulationEnvironment) config.getEnvironment()).getPlatform().killComponent();
        });
    }
}
