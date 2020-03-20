package de.unileipzig.irpact.io.config;

import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.agent.policy.NoTaxes;
import de.unileipzig.irpact.core.network.*;
import de.unileipzig.irpact.core.network.topology.ConstantTopology;
import de.unileipzig.irpact.core.network.topology.EgoistTopology;
import de.unileipzig.irpact.core.network.topology.UnchangingEdgeWeight;
import de.unileipzig.irpact.core.preference.ValueConfiguration;
import de.unileipzig.irpact.core.simulation.BasicEconomicSpace;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.dim2.SquareModel;
import de.unileipzig.irpact.jadex.message.JadexMessageSystem;
import de.unileipzig.irpact.jadex.simulation.BasicJadexEventManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationConfiguration;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Daniel Abitz
 */
public class JadexConfigurationBuilder extends AbstractConfigurationBuilder<JadexConfigurationBuilder> {

    public JadexConfigurationBuilder() {
    }

    @Override
    public JadexConfigurationBuilder initMinimal() {
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.setEventManager(new BasicJadexEventManager());
        env.setAgentNetwork(new BasicAgentNetwork(
                new BasicSocialGraph(
                        new DirectedMultiGraph<>(new HashMap<>()),
                        new HashMap<>()
                ),
                new BasicGraphConfiguration(
                        EgoistTopology.INSTANCE,
                        ConstantTopology.INSTANCE,
                        UnchangingEdgeWeight.INSTANCE
                )
        ));
        env.setMessageSystem(new JadexMessageSystem(env, JadexMessageSystem.Mode.BASIC));
        env.setSpatialModel(new SquareModel("Square", Metric.EUCLIDEAN, 0, 0, 1, 1));
        env.setEconomicSpace(new BasicEconomicSpace(
                NoTaxes.INSTANCE
        ));
        env.setConfig(new BasicJadexSimulationConfiguration(
                new BasicConsumerAgentGroupAffinitiesMapping(new HashMap<>()),
                new ValueConfiguration<>(new HashMap<>(), new HashSet<>()),
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>(),
                new HashSet<>(),
                new HashMap<>()
        ));
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
                policyAgents
        );
    }
}
