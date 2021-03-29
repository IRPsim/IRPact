package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.product.awareness.ProductAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.product.interest.ProductInterestSupplyScheme;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.commons.Derivable;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexConsumerAgentGroup extends SimulationEntityBase implements ConsumerAgentGroup {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexConsumerAgentGroup.class);

    protected int nextAgentId = 0;
    protected double informationAuthority;
    protected int maxNumberOfActions;
    protected SpatialDistribution spatialDistribution;
    protected Map<String, ConsumerAgentGroupAttribute> attributes;
    protected Map<String, ProductRelatedConsumerAgentGroupAttribute> productRelatedAttributes;
    protected Map<String, ConsumerAgent> agents;
    protected ProductAwarenessSupplyScheme awarenessSupplyScheme;
    protected ProductInterestSupplyScheme interestsSupplyScheme;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;

    protected Set<Need> initialNeeds;

    public JadexConsumerAgentGroup() {
        this(CollectionUtil.newMap(), CollectionUtil.newMap(), CollectionUtil.newMap(), CollectionUtil.newSet());
    }

    public JadexConsumerAgentGroup(
            Map<String, ConsumerAgentGroupAttribute> attributes,
            Map<String, ProductRelatedConsumerAgentGroupAttribute> productRelatedAttributes,
            Map<String, ConsumerAgent> agents,
            Set<Need> initialNeeds) {
        this.attributes = attributes;
        this.productRelatedAttributes = productRelatedAttributes;
        this.agents = agents;
        this.initialNeeds = initialNeeds;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getInformationAuthority(),
                getNextAgentId(),

                getSpatialDistribution().getChecksum(),
                ChecksumComparable.getCollChecksum(getAttributes()),
                getAwarenessSupplyScheme().getChecksum(),
                getInterestSupplyScheme().getChecksum(),
                getProductFindingScheme().getChecksum(),
                getProcessFindingScheme().getChecksum(),

                ChecksumComparable.getCollChecksum(getAgents())
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

    @Override
    public int getMaxNumberOfActions() {
        return maxNumberOfActions;
    }

    public void setMaxNumberOfActions(int maxNumberOfActions) {
        this.maxNumberOfActions = maxNumberOfActions;
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

    public void removeGroupAttribute(ConsumerAgentGroupAttribute attribute) {
        attributes.remove(attribute.getName());
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
    public Collection<ProductRelatedConsumerAgentGroupAttribute> getProductRelatedGroupAttributes() {
        return productRelatedAttributes.values();
    }

    @Override
    public boolean hasProductRelatedGroupAttribute(String name) {
        return productRelatedAttributes.containsKey(name);
    }

    @Override
    public ProductRelatedConsumerAgentGroupAttribute getProductRelatedGroupAttribute(String name) {
        return productRelatedAttributes.get(name);
    }

    @Override
    public void addProductRelatedGroupAttribute(ProductRelatedConsumerAgentGroupAttribute attribute) {
        if(hasProductRelatedGroupAttribute(attribute)) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' already exists", attribute.getName());
        }
        productRelatedAttributes.put(attribute.getName(), attribute);
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

    public void setInterestSupplyScheme(ProductInterestSupplyScheme awarenessSupplyScheme) {
        this.interestsSupplyScheme = awarenessSupplyScheme;
    }

    @Override
    public ProductInterestSupplyScheme getInterestSupplyScheme() {
        return interestsSupplyScheme;
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

    public Set<Need> getInitialNeeds() {
        return initialNeeds;
    }

    public boolean hasInitialNeed(Need need) {
        return initialNeeds.contains(need);
    }

    public void addInitialNeed(Need need) {
        initialNeeds.add(need);
    }

    protected synchronized int nextId() {
        int nextId = nextAgentId;
        nextAgentId++;
        return nextId;
    }

    protected Set<ConsumerAgentAttribute> deriveAttributes() {
        return getAttributes().stream()
                .map(Derivable::derive)
                .collect(CollectionUtil.collectToLinkedSet());
    }

    protected ProductAwareness deriveAwareness() {
        return awarenessSupplyScheme.derive();
    }

    protected ProductInterest deriveInterest() {
        return interestsSupplyScheme.derive();
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
        agent.setMaxNumberOfActions(getMaxNumberOfActions());
        agent.addAllAttributes(deriveAttributes());
        agent.setSpatialInformation(spatialInformation);
        agent.setProductAwareness(deriveAwareness());
        agent.setProductInterest(deriveInterest());
        agent.setProcessFindingScheme(getProcessFindingScheme());
        agent.setProductFindingScheme(getProductFindingScheme());

        for(Need need: getInitialNeeds()) {
            agent.addNeed(need);
        }

        agent.linkAccess(spatialInformation.getAttributeAccess());
        return agent;
    }
}
