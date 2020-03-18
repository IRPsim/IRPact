package de.unileipzig.irpact.io.config;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class Configuration {

    //extra
    protected LogConfig logConfig;
    //core
    protected SimulationEnvironment environment;
    protected Set<ConsumerAgentGroup> consumerAgentGroups;
    protected Set<CompanyAgent> companyAgents;
    protected Set<PointOfSaleAgent> pointOfSaleAgents;
    protected Set<PolicyAgent> policyAgents;

    public Configuration(
            LogConfig logConfig,
            SimulationEnvironment environment,
            Set<ConsumerAgentGroup> consumerAgentGroups,
            Set<CompanyAgent> companyAgents,
            Set<PointOfSaleAgent> pointOfSaleAgents,
            Set<PolicyAgent> policyAgents) {
        this.logConfig = logConfig;
        this.environment = environment;
        this.consumerAgentGroups = consumerAgentGroups;
        this.companyAgents = companyAgents;
        this.pointOfSaleAgents = pointOfSaleAgents;
        this.policyAgents = policyAgents;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public Set<ConsumerAgentGroup> getConsumerAgentGroups() {
        return consumerAgentGroups;
    }

    public Set<CompanyAgent> getCompanyAgents() {
        return companyAgents;
    }

    public Set<PointOfSaleAgent> getPointOfSaleAgents() {
        return pointOfSaleAgents;
    }

    public Set<PolicyAgent> getPolicyAgents() {
        return policyAgents;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }
}
