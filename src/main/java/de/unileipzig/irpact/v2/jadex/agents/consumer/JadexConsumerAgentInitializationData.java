package de.unileipzig.irpact.v2.jadex.agents.consumer;

import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentInitializationData;
import de.unileipzig.irpact.v2.core.product.Product;
import de.unileipzig.irpact.v2.core.spatial.SpatialInformation;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.service.annotation.Reference;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexConsumerAgentInitializationData implements ConsumerAgentInitializationData {

    public JadexConsumerAgentInitializationData() {
    }

    protected String name;
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    protected JadexSimulationEnvironment environment;
    public void setEnvironment(JadexSimulationEnvironment environment) {
        this.environment = environment;
    }
    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }

    protected SpatialInformation spatialInformation;
    public void setSpatialInformation(SpatialInformation spatialInformation) {
        this.spatialInformation = spatialInformation;
    }
    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }

    protected double informationAuthority;
    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }
    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }

    protected ConsumerAgentGroup group;
    public void setGroup(ConsumerAgentGroup group) {
        this.group = group;
    }
    @Override
    public ConsumerAgentGroup getGroup() {
        return group;
    }

    protected Set<ConsumerAgentAttribute> attributes;
    public void setAttributes(Set<ConsumerAgentAttribute> attributes) {
        this.attributes = attributes;
    }
    @Override
    public Set<ConsumerAgentAttribute> getAttributes() {
        return attributes;
    }

    protected Awareness<Product> productAwareness;
    public void setProductAwareness(Awareness<Product> productAwareness) {
        this.productAwareness = productAwareness;
    }
    @Override
    public Awareness<Product> getProductAwareness() {
        return productAwareness;
    }
}
