package de.unileipzig.irpact.jadex.config;

import de.unileipzig.irpact.core.config.AbstractConfigurationBuilder;
import de.unileipzig.irpact.core.config.LogConfig;
import de.unileipzig.irpact.core.network.*;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class JadexConfigurationBuilder extends AbstractConfigurationBuilder<JadexConfigurationBuilder> {

    public JadexConfigurationBuilder() {
    }

    @Override
    public JadexConfigurationBuilder initMinimal() {
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        BasicAgentNetwork network = new BasicAgentNetwork(
                new SocialGraph(),
                new BasicGraphConfiguration(
                        EgoistTopology.INSTANCE,
                        ConstantTopology.INSTANCE,
                        UnchangingEdgeWeight.INSTANCE
                )
        );
        env.setAgentNetwork(network);
        return setLogConfig(new JadexLogConfig())
                .setEnvironment(env);
    }

    @Override
    protected JadexConfigurationBuilder getThis() {
        return this;
    }

    public JadexConfigurationBuilder setEnvironment(JadexSimulationEnvironment environment) {
        return super.setEnvironment(environment);
    }

    @Override
    public JadexConfigurationBuilder setEnvironment(SimulationEnvironment environment) {
        if(environment instanceof JadexSimulationEnvironment) {
            return super.setEnvironment(environment);
        } else {
            throw new IllegalArgumentException("requires JadexLogConfig");
        }
    }

    public JadexConfigurationBuilder setLogConfig(JadexLogConfig logConfig) {
        return super.setLogConfig(logConfig);
    }

    @Override
    public JadexConfigurationBuilder setLogConfig(LogConfig logConfig) {
        if(logConfig instanceof JadexLogConfig) {
            return super.setLogConfig(logConfig);
        } else {
            throw new IllegalArgumentException("requires JadexLogConfig");
        }
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) super.getEnvironment();
    }

    @Override
    public JadexLogConfig getLogConfig() {
        return (JadexLogConfig) super.getLogConfig();
    }

    @Override
    public JadexConfiguration build() {
        return new JadexConfiguration(
                getLogConfig(),
                getEnvironment(),
                consumerAgentGroups,
                companyAgents,
                pointOfSaleAgents,
                policyAgents,
                productGroups
        );
    }
}
