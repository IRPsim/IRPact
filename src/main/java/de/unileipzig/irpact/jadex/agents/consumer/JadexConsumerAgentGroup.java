package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.commons.Derivable;
import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.commons.awareness.AwarenessDistributionMapping;
import de.unileipzig.irpact.commons.awareness.BasicAwarenessDistributionMapping;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexConsumerAgentGroup extends SimulationEntityBase implements ConsumerAgentGroup {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexConsumerAgentGroup.class);

    protected int agentId = 0;
    protected double informationAuthority;
    protected SpatialDistribution spatialDistribution;
    protected Map<String, ConsumerAgentGroupAttribute> attributes;
    protected Map<String, ConsumerAgent> agents;
    protected AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> fixedProductAwarenessMapping = new BasicAwarenessDistributionMapping<>();
    protected Awareness<Product> productAwareness;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;

    public JadexConsumerAgentGroup() {
        this(new HashMap<>(), new HashMap<>());
    }

    public JadexConsumerAgentGroup(
            Map<String, ConsumerAgentGroupAttribute> attributes,
            Map<String, ConsumerAgent> agents) {
        this.attributes = attributes;
        this.agents = agents;
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) super.getEnvironment();
    }

    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }

    @Override
    public void addGroupAttribute(ConsumerAgentGroupAttribute attribute) {
        if(hasGroupAttribute(attribute.getName())) {
            throw new IllegalArgumentException("attribute '" + attribute.getName() + "' already exists");
        }
        attributes.put(attribute.getName(), attribute);
    }

    @Override
    public boolean hasGroupAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public Collection<ConsumerAgentGroupAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public boolean addAgent(ConsumerAgent agent) {
        if(agent.getGroup() != this) {
            throw new IllegalArgumentException();
        }
        if(agents.containsKey(agent.getName())) {
            return false;
        } else {
            agents.put(agent.getName(), agent);
            return true;
        }
    }

    @Override
    public void replace(ConsumerAgent toRemove, ConsumerAgent toAdd) throws IllegalStateException {
        if(agents.get(toRemove.getName()) == toRemove) {
            if(!Objects.equals(toRemove.getName(), toAdd.getName()) && hasName(toAdd.getName())) {
                throw new IllegalStateException("name '" + toAdd.getName() + "' already exists");
            }
            agents.remove(toRemove.getName());
            agents.put(toAdd.getName(), toAdd);
        } else {
            throw new IllegalArgumentException("name '" + toRemove.getName() + "' does not exist");
        }
    }

    @Override
    public Collection<ConsumerAgent> getAgents() {
        return agents.values();
    }

    public void setSpatialDistribution(SpatialDistribution spatialDistribution) {
        this.spatialDistribution = spatialDistribution;
    }

    @Override
    public SpatialDistribution getSpatialDistribution() {
        return spatialDistribution;
    }

    public void setProductAwareness(Awareness<Product> productAwareness) {
        this.productAwareness = productAwareness;
    }

    @Override
    public Awareness<Product> getProductAwareness() {
        return productAwareness;
    }

    public void setFixedProductAwarenessMapping(AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> fixedProductAwareness) {
        this.fixedProductAwarenessMapping = fixedProductAwareness;
    }

    @Override
    public AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> getFixedProductAwarenessMapping() {
        return fixedProductAwarenessMapping;
    }

    @Override
    public ProcessFindingScheme getProcessFindingScheme() {
        return processFindingScheme;
    }

    public void setProcessFindingScheme(ProcessFindingScheme processFindingScheme) {
        this.processFindingScheme = processFindingScheme;
    }

    @Override
    public ProductFindingScheme getProductFindingScheme() {
        return productFindingScheme;
    }

    public void setProductFindingScheme(ProductFindingScheme productFindingScheme) {
        this.productFindingScheme = productFindingScheme;
    }

    protected synchronized int nextId() {
        int nextId = agentId;
        agentId++;
        return nextId;
    }

    protected Set<ConsumerAgentAttribute> deriveAttributes() {
        return getAttributes().stream()
                .map(Derivable::derive)
                .collect(Collectors.toSet());
    }

    protected Awareness<Product> deriveAwareness() {
        Awareness<Product> awareness = getProductAwareness().emptyCopy();
        addFixedProductAwareness(awareness);
        return awareness;
    }

    protected void addFixedProductAwareness(Awareness<Product> awareness) {
        for(ProductGroup pgrp: getEnvironment().getProducts().getGroups()) {
            for(Product fixedProduct: pgrp.getFixedProducts()) {
                UnivariateDoubleDistribution dist = getFixedProductAwarenessMapping().getDistribution(fixedProduct);
                if(dist == null) {
                    throw new NoSuchElementException("no dist for '" + fixedProduct.getName() + "'");
                }
                double value = dist.drawDoubleValue();
                awareness.update(fixedProduct, value);
            }
        }
    }

    @Override
    public PlaceholderConsumerAgent deriveAgent() {
        PlaceholderConsumerAgent agent = new PlaceholderConsumerAgent();
        agent.setName(getName() + "_" + nextId());
        agent.setGroup(this);
        agent.setEnvironment(getEnvironment());
        agent.setInformationAuthority(getInformationAuthority());
        agent.addAttributes(deriveAttributes());
        agent.setSpatialInformation(getSpatialDistribution().drawValue());
        agent.setProductAwareness(deriveAwareness());
        agent.setProcessFindingScheme(getProcessFindingScheme());
        agent.setProductFindingScheme(getProductFindingScheme());
        return agent;
    }
}
