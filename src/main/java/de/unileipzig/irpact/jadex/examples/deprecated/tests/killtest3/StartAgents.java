package de.unileipzig.irpact.jadex.examples.deprecated.tests.killtest3;

import de.unileipzig.irpact.jadex.config.JadexConfiguration;
import de.unileipzig.irpact.jadex.config.JadexConfigurationBuilder;
import de.unileipzig.irpact.jadex.config.JadexLogConfig;
import de.unileipzig.irpact.jadex.examples.deprecated.tests.configs.AllAgentsAnd2Groups;
import de.unileipzig.irpact.jadex.start.StartSimulation;

/**
 * @author Daniel Abitz
 */
public class StartAgents {

    public static void main(String[] args) {
        JadexConfigurationBuilder cb = AllAgentsAnd2Groups.builder()
                .setLogConfig(new JadexLogConfig());
        JadexConfiguration config = cb.validate().build();
        StartSimulation.start(config);
    }
}
