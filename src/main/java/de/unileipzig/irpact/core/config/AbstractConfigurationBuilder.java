package de.unileipzig.irpact.core.config;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConfigurationBuilder<T extends AbstractConfigurationBuilder<T>> {

    protected SimulationEnvironment environment;
    protected LogConfig logConfig;
    protected Map<String, Need> needs = new HashMap<>();
    protected Map<String, ProductGroupAttribute> pgAttributes = new HashMap<>();
    protected Map<String, ConsumerAgentGroupAttribute> cagAttributes = new HashMap<>();

    protected Set<ConsumerAgentGroup> consumerAgentGroups = new HashSet<>();
    protected Set<CompanyAgent> companyAgents = new HashSet<>();
    protected Set<PointOfSaleAgent> pointOfSaleAgents = new HashSet<>();
    protected Set<PolicyAgent> policyAgents = new HashSet<>();
    protected Set<ProductGroup> productGroups = new HashSet<>();

    public AbstractConfigurationBuilder() {
    }

    //=========================
    //util
    //=========================

    public T reset() {
        needs.clear();
        pgAttributes.clear();
        cagAttributes.clear();
        consumerAgentGroups.clear();
        companyAgents.clear();
        pointOfSaleAgents.clear();
        policyAgents.clear();
        productGroups.clear();
        return getThis();
    }

    public T validateDeep() {
        validate();
        return getThis();
    }

    public T validate() {
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

    public T addProductGroup(ProductGroup pg) {
        productGroups.add(pg);
        return getThis();
    }

    //=========================
    //get
    //=========================

    public SimulationEnvironment getEnvironment() {
        return environment;
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
