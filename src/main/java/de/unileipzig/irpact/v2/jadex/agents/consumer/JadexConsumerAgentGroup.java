package de.unileipzig.irpact.v2.jadex.agents.consumer;

import de.unileipzig.irpact.v2.commons.attribute.AttributeGroup;
import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irpact.v2.commons.awareness.AwarenessDistributionMapping;
import de.unileipzig.irpact.v2.commons.awareness.BasicAwarenessDistributionMapping;
import de.unileipzig.irpact.v2.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.v2.core.product.Product;
import de.unileipzig.irpact.v2.core.product.ProductGroup;
import de.unileipzig.irpact.v2.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.v2.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexConsumerAgentGroup extends SimulationEntityBase implements ConsumerAgentGroup {

    protected int agentId = 0;
    protected double informationAuthority;
    protected SpatialDistribution spatialDistribution;
    protected Set<ConsumerAgentGroupAttribute> attributes;
    protected Set<ConsumerAgent> agents;
    protected AwarenessDistributionMapping<Product, UnivariateDoubleDistribution> fixedProductAwarenessMapping = new BasicAwarenessDistributionMapping<>();
    protected Awareness<Product> productAwareness;

    public JadexConsumerAgentGroup() {
        this(new HashSet<>(), new HashSet<>());
    }

    public JadexConsumerAgentGroup(
            Set<ConsumerAgentGroupAttribute> attributes,
            Set<ConsumerAgent> agents) {
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

    public boolean addAttribute(ConsumerAgentGroupAttribute attribute) {
        return attributes.add(attribute);
    }

    @Override
    public Set<ConsumerAgentGroupAttribute> getAttributes() {
        return attributes;
    }

    public boolean addAgent(ConsumerAgent agent) {
        if(agent.getGroup() != this) {
            throw new IllegalArgumentException();
        }
        return agents.add(agent);
    }

    @Override
    public Set<ConsumerAgent> getAgents() {
        return agents;
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

    protected synchronized int nextId() {
        int nextId = agentId;
        agentId++;
        return nextId;
    }

    protected Set<ConsumerAgentAttribute> deriveAttributes() {
        return attributes.stream()
                .map(AttributeGroup::derive)
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
    public JadexConsumerAgentInitializationData derive() {
        JadexConsumerAgentInitializationData data = new JadexConsumerAgentInitializationData();
        data.setName(getName() + "_" + nextId());
        data.setGroup(this);
        data.setEnvironment(getEnvironment());
        data.setInformationAuthority(getInformationAuthority());
        data.setAttributes(deriveAttributes());
        data.setSpatialInformation(getSpatialDistribution().drawValue());
        data.setProductAwareness(deriveAwareness());
        return data;
    }
}
