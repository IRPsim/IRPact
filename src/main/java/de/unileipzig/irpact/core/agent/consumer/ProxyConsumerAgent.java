package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.core.agent.ProxyAgent;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class ProxyConsumerAgent extends SpatialInformationAgentBase implements ConsumerAgent, ProxyAgent<ConsumerAgent> {

    protected ConsumerAgent realAgent;

    protected ConsumerAgentGroup group;
    protected SocialGraph.Node node;
    protected Map<String, ConsumerAgentAttribute> attributes;
    protected ProductAwareness awareness;
    protected Set<AdoptedProduct> adoptedProducts;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;
    protected Set<Need> needs;
    protected Map<Need, ProcessPlan> plans;
    protected Set<AttributeAccess> externAttributes;

    public ProxyConsumerAgent() {
        this(new HashMap<>(), new HashSet<>(), new HashSet<>(), new HashMap<>(), new HashSet<>());
    }

    public ProxyConsumerAgent(
            Map<String, ConsumerAgentAttribute> attributes,
            Set<AdoptedProduct> adoptedProducts,
            Set<Need> needs,
            Map<Need, ProcessPlan> plans,
            Set<AttributeAccess> externAttributes) {
        this.attributes = attributes;
        this.adoptedProducts = adoptedProducts;
        this.needs = needs;
        this.plans = plans;
        this.externAttributes = externAttributes;
    }

    @Override
    public boolean isSynced() {
        return realAgent != null;
    }

    public boolean isNotSynced() {
        return realAgent == null;
    }

    public void sync(ConsumerAgent realAgent) {
        if(isSynced()) {
            throw new IllegalStateException("already synced");
        }
        this.realAgent = realAgent;
        clear();
    }

    protected void clear() {
        group = null;
        node = null;
        attributes = null;
        awareness = null;
        adoptedProducts = null;
        productFindingScheme = null;
        processFindingScheme = null;
        needs = null;
        plans = null;
    }

    @Override
    public ConsumerAgent getRealAgent() {
        return realAgent;
    }

    protected void checkSynced() {
        if(isNotSynced()) {
            throw new IllegalStateException("not synced");
        }
    }

    protected void checkNotSynced() {
        if(isSynced()) {
            throw new IllegalStateException("synced");
        }
    }

    @Override
    public void lockAction() {
        checkSynced();
        getRealAgent().lockAction();
    }

    @Override
    public void actionPerformed() {
        checkSynced();
        getRealAgent().actionPerformed();
    }

    @Override
    public void releaseAction() {
        checkSynced();
        getRealAgent().releaseAction();
    }

    @Override
    public boolean aquireAction() {
        checkSynced();
        return getRealAgent().aquireAction();
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        if(isSynced()) {
            return getRealAgent().getEnvironment();
        } else {
            return environment;
        }
    }

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        checkNotSynced();
        this.environment = environment;
    }

    @Override
    public String getName() {
        if(isSynced()) {
            return getRealAgent().getName();
        } else {
            return name;
        }
    }

    @Override
    public void setName(String name) {
        checkNotSynced();
        this.name = name;
    }

    @Override
    public double getInformationAuthority() {
        if(isSynced()) {
            return getRealAgent().getInformationAuthority();
        } else {
            return informationAuthority;
        }
    }

    @Override
    public void setInformationAuthority(double informationAuthority) {
        checkNotSynced();
        this.informationAuthority = informationAuthority;
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        if(isSynced()) {
            return getRealAgent().getSpatialInformation();
        } else {
            return spatialInformation;
        }
    }

    @Override
    public void setSpatialInformation(SpatialInformation spatialInformation) {
        checkNotSynced();
        this.spatialInformation = spatialInformation;
    }

    @Override
    public SocialGraph.Node getSocialGraphNode() {
        if(isSynced()) {
            return getRealAgent().getSocialGraphNode();
        } else {
            return node;
        }
    }

    @Override
    public void setSocialGraphNode(SocialGraph.Node node) {
        if(isSynced()) {
            getRealAgent().setSocialGraphNode(node);
        } else {
            this.node = node;
        }
    }

    @Override
    public ConsumerAgentGroup getGroup() {
        if(isSynced()) {
            return getRealAgent().getGroup();
        } else {
            return group;
        }
    }

    public void setGroup(ConsumerAgentGroup group) {
        this.group = group;
    }

    @Override
    public Collection<ConsumerAgentAttribute> getAttributes() {
        if(isSynced()) {
            return getRealAgent().getAttributes();
        } else {
            return attributes.values();
        }
    }

    @Override
    public ConsumerAgentAttribute getAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().getAttribute(name);
        } else {
            return attributes.get(name);
        }
    }

    @Override
    public boolean hasAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().hasAttribute(name);
        } else {
            return attributes.containsKey(name);
        }
    }

    public void addAllAttributes(Collection<? extends ConsumerAgentAttribute> attributes) {
        for(ConsumerAgentAttribute cagAttr: attributes) {
            addAttribute(cagAttr);
        }
    }

    public void addAllAttributes(ConsumerAgentAttribute... attributes) {
        for(ConsumerAgentAttribute cagAttr: attributes) {
            addAttribute(cagAttr);
        }
    }

    @Override
    public void addAttribute(ConsumerAgentAttribute attribute) {
        if(isSynced()) {
            getRealAgent().addAttribute(attribute);
        } else {
            if(attributes.containsKey(attribute.getName())) {
                throw new IllegalArgumentException("attribute '" + attribute.getName() + "' already exists");
            } else {
                attributes.put(attribute.getName(), attribute);
            }
        }
    }

    @Override
    public ProductAwareness getProductAwareness() {
        if(isSynced()) {
            return getRealAgent().getProductAwareness();
        } else {
            return awareness;
        }
    }

    public void setProductAwareness(ProductAwareness awareness) {
        this.awareness = awareness;
    }

    @Override
    public Collection<AdoptedProduct> getAdoptedProducts() {
        if(isSynced()) {
            return getRealAgent().getAdoptedProducts();
        } else {
            return adoptedProducts;
        }
    }

    @Override
    public boolean hasAdopted(Product product) {
        if(isSynced()) {
            return getRealAgent().hasAdopted(product);
        } else {
            for(AdoptedProduct ap: adoptedProducts) {
                if(ap.getProduct() == product) {
                    return true;
                }
            }
            return false;
        }
    }

    public void addAllAdoptedProducts(AdoptedProduct... adoptedProducts) {
        for(AdoptedProduct adoptedProduct: adoptedProducts) {
            addAdoptedProduct(adoptedProduct);
        }
    }

    @Override
    public void addAdoptedProduct(AdoptedProduct adoptedProduct) {
        if(isSynced()) {
            getRealAgent().addAdoptedProduct(adoptedProduct);
        } else {
            adoptedProducts.add(adoptedProduct);
        }
    }

    @Override
    public void adopt(Need need, Product product) {
        checkSynced();
        getRealAgent().adopt(need, product);
    }

    public ProductFindingScheme getProductFindingScheme() {
        return productFindingScheme;
    }

    public void setProductFindingScheme(ProductFindingScheme productFindingScheme) {
        this.productFindingScheme = productFindingScheme;
    }

    public ProcessFindingScheme getProcessFindingScheme() {
        return processFindingScheme;
    }

    public void setProcessFindingScheme(ProcessFindingScheme processFindingScheme) {
        this.processFindingScheme = processFindingScheme;
    }

    public Set<Need> getNeeds() {
        return needs;
    }

    public void addAllNeeds(Need... needs) {
        this.needs.addAll(Arrays.asList(needs));
    }

    public Map<Need, ProcessPlan> getPlans() {
        return plans;
    }

    public void addAllPlans(Map<Need, ProcessPlan> plans) {
        this.plans.putAll(plans);
    }

    @Override
    public boolean link(AttributeAccess attributeAccess) {
        if(isSynced()) {
            return getRealAgent().link(attributeAccess);
        } else {
            return externAttributes.add(attributeAccess);
        }
    }

    @Override
    public boolean unlink(AttributeAccess attributeAccess) {
        if(isSynced()) {
            return getRealAgent().unlink(attributeAccess);
        } else {
            return externAttributes.remove(attributeAccess);
        }
    }

    @Override
    public Attribute<?> findAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().findAttribute(name);
        } else {
            ConsumerAgentAttribute attr = getAttribute(name);
            if(attr != null) {
                return attr;
            }
            for(AttributeAccess attributeAccess: externAttributes) {
                if(attributeAccess.hasAttribute(name)) {
                    return attributeAccess.getAttribute(name);
                }
            }
            return null;
        }
    }

    public Collection<AttributeAccess> getExternAttributes() {
        return externAttributes;
    }
}
