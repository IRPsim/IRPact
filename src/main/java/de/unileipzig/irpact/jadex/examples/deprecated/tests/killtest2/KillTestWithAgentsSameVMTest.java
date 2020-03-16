package de.unileipzig.irpact.jadex.examples.deprecated.tests.killtest2;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.config.JadexConfiguration;
import de.unileipzig.irpact.jadex.config.JadexConfigurationBuilder;
import de.unileipzig.irpact.jadex.config.JadexLogConfig;
import de.unileipzig.irpact.jadex.examples.deprecated.tests.configs.AllAgentsAnd2Groups;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import de.unileipzig.irpact.jadex.start.StopSimulation;

/**
 * @author Daniel Abitz
 */
public class KillTestWithAgentsSameVMTest {

    public static void main(String[] args) {
        JadexConfigurationBuilder cb = AllAgentsAnd2Groups.builder()
                .setLogConfig(new JadexLogConfig());
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
            config.getEnvironment().getLogger().info("call kill");
            ConcurrentUtil.start(0, () ->StopSimulation.main(new String[0]));
        });
    }
}
