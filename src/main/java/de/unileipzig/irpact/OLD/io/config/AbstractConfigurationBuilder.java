package de.unileipzig.irpact.OLD.io.config;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.BasicSimulationConfiguration;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConfigurationBuilder<T extends AbstractConfigurationBuilder<T>> {

    //helper
    protected Map<String, Need> needs = new HashMap<>();
    protected Map<String, ProductGroupAttribute> pgAttributes = new HashMap<>();
    protected Map<String, ConsumerAgentGroupAttribute> cagAttributes = new HashMap<>();
    //config
    protected LogConfig logConfig;
    protected SimulationEnvironment environment;
    protected Set<ConsumerAgentGroup> consumerAgentGroups = new HashSet<>();
    protected Set<CompanyAgent> companyAgents = new HashSet<>();
    protected Set<PointOfSaleAgent> pointOfSaleAgents = new HashSet<>();
    protected Set<PolicyAgent> policyAgents = new HashSet<>();

    public AbstractConfigurationBuilder() {
    }

    //=========================
    //util
    //=========================

    public T reset() {
        //helper
        needs = new HashMap<>();
        pgAttributes = new HashMap<>();
        cagAttributes = new HashMap<>();
        //config
        logConfig = null;
        environment = null;
        consumerAgentGroups = new HashSet<>();
        companyAgents = new HashSet<>();
        pointOfSaleAgents = new HashSet<>();
        policyAgents = new HashSet<>();
        return getThis();
    }

    public T validateDeep() {
        validate();
        Check.requireNonNull(environment.getSpatialModel(), "spatialModel");
        Check.requireNonNull(environment.getAgentNetwork(), "agentNetwork");
        Check.requireNonNull(environment.getEconomicSpace(), "economicSpace");
        Check.requireNonNull(environment.getMessageSystem(), "messageSystem");
        //Check.requireNonNull(timeModule, "timeModule");
        Check.requireNonNull(environment.getConfiguration(), "simulationConfig");
        //Check.requireNonNull(logger, "logger");
        return getThis();
    }

    public T validate() {
        Check.requireNonNull(logConfig, "logConfig");
        Check.requireNonNull(environment, "environment");
        return getThis();
    }

    public abstract Configuration build();

    public abstract T initMinimal();

    protected abstract T getThis();

    //=========================
    //basic
    //=========================

    public T setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
        return getThis();
    }

    public T setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
        return getThis();
    }

    public T addNeed(String name, Need need) {
        needs.put(name, need);
        return getThis();
    }

    public T addProductGroupAttribute(String name, ProductGroupAttribute pgAttribute) {
        pgAttributes.put(name, pgAttribute);
        return getThis();
    }

    public T addConsumerAgentGroupAttribute(String name, ConsumerAgentGroupAttribute cagAttribute) {
        cagAttributes.put(name, cagAttribute);
        return getThis();
    }

    public T addConsumerAgentGroup(ConsumerAgentGroup cag) {
        consumerAgentGroups.add(cag);
        return getThis();
    }

    public T addCompanyAgent(CompanyAgent ca) {
        companyAgents.add(ca);
        return getThis();
    }

    public T addPointOfSaleAgent(PointOfSaleAgent posa) {
        pointOfSaleAgents.add(posa);
        return getThis();
    }

    public T addPolicyAgent(PolicyAgent pa) {
        policyAgents.add(pa);
        return getThis();
    }

    public T addProductGroup(ProductGroup group) {
        BasicSimulationConfiguration config = (BasicSimulationConfiguration) environment.getConfiguration();
        if(!config.add(group)) {
            throw new IllegalStateException("already exists: " + group.getName());
        }
        return getThis();
    }

    //=========================
    //get
    //=========================

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    @SuppressWarnings("unchecked")
    public <R extends SpatialModel> R getSpatialModel() {
        SpatialModel model = environment.getSpatialModel();
        if(model == null) {
            throw new NoSuchElementException();
        }
        return (R) model;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public Set<Need> getNeeds(Collection<String> names) {
        return needs.entrySet()
                .stream()
                .filter(_entry -> names.contains(_entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public Set<Need> getNeeds(String... names) {
        return getNeeds(Arrays.asList(names));
    }

    public Set<ProductGroupAttribute> getProductGroupAttributes(Collection<String> names) {
        return pgAttributes.entrySet()
                .stream()
                .filter(_entry -> names.contains(_entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public Set<ProductGroupAttribute> getProductGroupAttributes(String... names) {
        return getProductGroupAttributes(Arrays.asList(names));
    }

    public Set<ConsumerAgentGroupAttribute> getConsumerAgentGroupAttributes(Collection<String> names) {
        return cagAttributes.entrySet()
                .stream()
                .filter(_entry -> names.contains(_entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public Set<ConsumerAgentGroupAttribute> getConsumerAgentGroupAttributes(String... names) {
        return getConsumerAgentGroupAttributes(Arrays.asList(names));
    }
}
