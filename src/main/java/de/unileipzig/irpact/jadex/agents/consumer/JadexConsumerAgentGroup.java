package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.product.awareness.ProductAwarenessSupplyScheme;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.commons.Derivable;
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

    protected int nextAgentId = 0;
    protected double informationAuthority;
    protected SpatialDistribution spatialDistribution;
    protected Map<String, ConsumerAgentGroupAttribute> attributes;
    protected Map<String, ConsumerAgent> agents;
    protected ProductAwarenessSupplyScheme awarenessSupplyScheme;
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

    public void addAllGroupAttributes(ConsumerAgentGroupAttribute... attributes) {
        for(ConsumerAgentGroupAttribute attr: attributes) {
            addGroupAttribute(attr);
        }
    }

    @Override
    public ConsumerAgentGroupAttribute getGroupAttribute(String name) {
        return attributes.get(name);
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

    public void setAwarenessSupplyScheme(ProductAwarenessSupplyScheme awarenessSupplyScheme) {
        this.awarenessSupplyScheme = awarenessSupplyScheme;
    }

    @Override
    public ProductAwarenessSupplyScheme getAwarenessSupplyScheme() {
        return awarenessSupplyScheme;
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
        int nextId = nextAgentId;
        nextAgentId++;
        return nextId;
    }

    protected Set<ConsumerAgentAttribute> deriveAttributes() {
        return getAttributes().stream()
                .map(Derivable::derive)
                .collect(Collectors.toSet());
    }

    protected ProductAwareness deriveAwareness() {
        return awarenessSupplyScheme.derive();
    }

    @Override
    public ProxyConsumerAgent deriveAgent() {
        SpatialInformation spatialInformation = getSpatialDistribution().drawValue();
        ProxyConsumerAgent agent = new ProxyConsumerAgent();
        agent.setName(getName() + "_" + nextId());
        agent.setGroup(this);
        agent.setEnvironment(getEnvironment());
        agent.setInformationAuthority(getInformationAuthority());
        agent.addAllAttributes(deriveAttributes());
        agent.setSpatialInformation(spatialInformation);
        agent.setProductAwareness(deriveAwareness());
        agent.setProcessFindingScheme(getProcessFindingScheme());
        agent.setProductFindingScheme(getProductFindingScheme());

        agent.link(spatialInformation.getAttributeAccess());
        return agent;
    }
}
