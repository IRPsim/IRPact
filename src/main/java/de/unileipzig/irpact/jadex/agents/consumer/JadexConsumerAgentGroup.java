package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.product.interest.ProductInterestSupplyScheme;
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
    protected ProductInterestSupplyScheme awarenessSupplyScheme;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;

    public JadexConsumerAgentGroup() {
        this(new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public JadexConsumerAgentGroup(
            Map<String, ConsumerAgentGroupAttribute> attributes,
            Map<String, ConsumerAgent> agents) {
        this.attributes = attributes;
        this.agents = agents;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getInformationAuthority(),
                getNextAgentId(),

                getSpatialDistribution().getHashCode(),
                IsEquals.getCollHashCode(getAttributes()),
                getAwarenessSupplyScheme().getHashCode(),
                getProductFindingScheme().getHashCode(),
                getProcessFindingScheme().getHashCode(),

                IsEquals.getCollHashCode(getAgents())
        );
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

    public void setAwarenessSupplyScheme(ProductInterestSupplyScheme awarenessSupplyScheme) {
        this.awarenessSupplyScheme = awarenessSupplyScheme;
    }

    @Override
    public ProductInterestSupplyScheme getAwarenessSupplyScheme() {
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

    protected ProductInterest deriveAwareness() {
        return awarenessSupplyScheme.derive();
    }

    public String deriveName() {
        return getName() + "_" + nextId();
    }

    public void setNextAgentId(int nextAgentId) {
        this.nextAgentId = nextAgentId;
    }

    public int getNextAgentId() {
        return nextAgentId;
    }

    @Override
    public ProxyConsumerAgent deriveAgent() {
        SpatialInformation spatialInformation = getSpatialDistribution().drawValue();
        ProxyConsumerAgent agent = new ProxyConsumerAgent();
        agent.setName(deriveName());
        agent.setGroup(this);
        agent.setEnvironment(getEnvironment());
        agent.setInformationAuthority(getInformationAuthority());
        agent.addAllAttributes(deriveAttributes());
        agent.setSpatialInformation(spatialInformation);
        agent.setProductAwareness(deriveAwareness());
        agent.setProcessFindingScheme(getProcessFindingScheme());
        agent.setProductFindingScheme(getProductFindingScheme());

        agent.linkAccess(spatialInformation.getAttributeAccess());
        return agent;
    }
}
