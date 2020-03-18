package de.unileipzig.irpact.io.config;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class JadexConfiguration extends Configuration {

    public JadexConfiguration(
            JadexLogConfig logConfig,
            JadexSimulationEnvironment environment,
            Set<ConsumerAgentGroup> consumerAgentGroups,
            Set<CompanyAgent> companyAgents,
            Set<PointOfSaleAgent> pointOfSaleAgents,
            Set<PolicyAgent> policyAgents) {
        super(
                logConfig,
                environment,
                consumerAgentGroups,
                companyAgents,
                pointOfSaleAgents,
                policyAgents
        );
    }

    @Override
    public JadexLogConfig getLogConfig() {
        return (JadexLogConfig) super.getLogConfig();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) super.getEnvironment();
    }
}
