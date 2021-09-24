package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.checksum.LoggableChecksum;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.ProxyAgent;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.develop.AddToPersist;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("DefaultAnnotationParam")
@AddToPersist("product related attr")
@Reference(local = true, remote = true)
public class ProxyConsumerAgent extends SpatialInformationAgentBase implements ConsumerAgent, ProxyAgent<ConsumerAgent>, LoggableChecksum {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProxyConsumerAgent.class);

    protected ConsumerAgent realAgent;

    protected ConsumerAgentGroup group;
    protected SocialGraph.Node node;
    protected Map<String, ConsumerAgentAttribute> attributes;
    protected Map<String, ConsumerAgentProductRelatedAttribute> productRelatedAttributes;
    protected ProductAwareness awareness;
    protected ProductInterest interest;
    protected Map<Product, AdoptedProduct> adoptedProducts;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;
    protected Set<Need> needs;
    protected Map<Need, ProcessPlan> activePlans;
    protected List<ProcessPlan> runningPlans;
    protected Set<AttributeAccess> externAttributes;

    public ProxyConsumerAgent() {
        this(
                new LinkedHashMap<>(),
                new LinkedHashMap<>(),
                new LinkedHashMap<>(),
                new LinkedHashSet<>(),
                new LinkedHashMap<>(),
                new ArrayList<>(),
                new LinkedHashSet<>()
        );
    }

    public ProxyConsumerAgent(
            Map<String, ConsumerAgentAttribute> attributes,
            Map<String, ConsumerAgentProductRelatedAttribute> productRelatedAttributes,
            Map<Product, AdoptedProduct> adoptedProducts,
            Set<Need> needs,
            Map<Need, ProcessPlan> activePlans,
            List<ProcessPlan> runningPlans,
            Set<AttributeAccess> externAttributes) {
        this.attributes = attributes;
        this.productRelatedAttributes = productRelatedAttributes;
        this.adoptedProducts = adoptedProducts;
        this.needs = needs;
        this.activePlans = activePlans;
        this.runningPlans = runningPlans;
        this.externAttributes = externAttributes;
    }

    @Override
    public int getChecksum() {
        if(isSynced()) {
            return getRealAgent().getChecksum();
        } else {
            return Checksums.SMART.getChecksum(
                    getName(),
                    getGroup().getName(),
                    getInformationAuthority(),
                    this.getActingOrder(),
                    getMaxNumberOfActions(),
                    getSpatialInformation(),
                    ChecksumComparable.getCollChecksum(getAttributes()),
                    ChecksumComparable.getCollChecksum(getProductRelatedAttributes()),
                    getProductAwareness(),
                    getProductInterest(),
                    ChecksumComparable.getCollChecksum(getAdoptedProducts()),
                    getProductFindingScheme(),
                    getProcessFindingScheme(),
                    ChecksumComparable.getCollChecksum(getNeeds()),
                    ChecksumComparable.getMapChecksum(getActivePlans()),
                    ChecksumComparable.getCollChecksum(getExternAttributes())
            );
        }
    }

    private static void logChecksum(String msg, int storedHash) {
        LOGGER.warn(
                "checksum @ '{}': stored={}",
                msg,
                Integer.toHexString(storedHash)
        );
    }

    @Override
    public void logChecksums() {
        logChecksum("name", ChecksumComparable.getChecksum(getName()));
        logChecksum("group name", ChecksumComparable.getChecksum(getGroup().getName()));
        logChecksum("information authority", ChecksumComparable.getChecksum(getInformationAuthority()));
        logChecksum("attention order", ChecksumComparable.getChecksum(this.getActingOrder()));
        logChecksum("max number of actions", ChecksumComparable.getChecksum(getMaxNumberOfActions()));
        logChecksum("spatial information", ChecksumComparable.getChecksum(getSpatialInformation()));
        logChecksum("attributes", ChecksumComparable.getCollChecksum(getAttributes()));
        logChecksum("product attributes", ChecksumComparable.getCollChecksum(getProductRelatedAttributes()));
        logChecksum("awareness", ChecksumComparable.getChecksum(getProductAwareness()));
        logChecksum("interest", ChecksumComparable.getChecksum(getProductInterest()));
        logChecksum("adopted products", ChecksumComparable.getCollChecksum(getAdoptedProducts()));
        logChecksum("product finding scheme", ChecksumComparable.getChecksum(getProductFindingScheme()));
        logChecksum("process finding scheme", ChecksumComparable.getChecksum(getProcessFindingScheme()));
        logChecksum("needs", ChecksumComparable.getCollChecksum(getNeeds()));
        logChecksum("plans", ChecksumComparable.getMapChecksum(getActivePlans()));
        logChecksum("external attributes", ChecksumComparable.getCollChecksum(getExternAttributes()));
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
        attributes.clear();
        productRelatedAttributes.clear();
        awareness = null;
        interest = null;
        adoptedProducts.clear();
        productFindingScheme = null;
        processFindingScheme = null;
        needs.clear();
        activePlans.clear();
        runningPlans.clear();
    }

    public void unsync(ConsumerAgent realAgent) {
        if(isNotSynced()) {
            throw new IllegalStateException("not synced");
        }
        if(this.realAgent != realAgent) {
            throw new IllegalArgumentException("synced to another agent");
        }
        LOGGER.trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] unsync", realAgent.getName());
        this.realAgent = null;
        reset(realAgent);
    }

    protected void reset(ConsumerAgent realAgent) {
        try {
            group = realAgent.getGroup();
            node = realAgent.getSocialGraphNode();
            addAllAttributes(realAgent.getAttributes());
            addAllProductRelatedAttribute(realAgent.getProductRelatedAttributes());
            awareness = realAgent.getProductAwareness();
            interest = realAgent.getProductInterest();
            addAllAdoptedProducts(realAgent.getAdoptedProducts());
            productFindingScheme = realAgent.getProductFindingScheme();
            processFindingScheme = realAgent.getProcessFindingScheme();
            needs.addAll(realAgent.getNeeds());
            addAllActivePlans(realAgent.getActivePlans());
            addAllRunningPlans(realAgent.getRunningPlans());
        } catch (Throwable t) {
            LOGGER.error("reset failed for agent '" + getName() + "'", t);
        }
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
    public void allowAttention() {
        checkSynced();
        getRealAgent().allowAttention();
    }

    @Override
    public boolean tryAquireAttention() {
        checkSynced();
        return getRealAgent().tryAquireAttention();
    }

    @Override
    public void actionPerformed() {
        checkSynced();
        getRealAgent().actionPerformed();
    }

    @Override
    public void releaseAttention() {
        checkSynced();
        getRealAgent().releaseAttention();
    }

    @Override
    public void aquireDataAccess() {
        checkSynced();
        getRealAgent().aquireDataAccess();
    }

    @Override
    public boolean tryAquireDataAccess() {
        checkSynced();
        return getRealAgent().tryAquireDataAccess();
    }

    @Override
    public boolean tryAquireDataAccess(long time, TimeUnit unit) throws InterruptedException {
        checkSynced();
        return getRealAgent().tryAquireDataAccess(time, unit);
    }

    @Override
    public void releaseDataAccess() {
        checkSynced();
        getRealAgent().releaseDataAccess();
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
    public void setActingOrder(long actingOrder) {
        checkNotSynced();
        this.actingOrder = actingOrder;
    }

    @Override
    public long getActingOrder() {
        if(isSynced()) {
            return getRealAgent().getActingOrder();
        } else {
            return actingOrder;
        }
    }

    @Override
    public int getMaxNumberOfActions() {
        if(isSynced()) {
            return getRealAgent().getMaxNumberOfActions();
        } else {
            return maxNumberOfActions;
        }
    }

    @Override
    public void setMaxNumberOfActions(int maxNumberOfActions) {
        checkNotSynced();
        this.maxNumberOfActions = maxNumberOfActions;
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
        checkNotSynced();
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
    public Collection<ConsumerAgentProductRelatedAttribute> getProductRelatedAttributes() {
        if(isSynced()) {
            return getRealAgent().getProductRelatedAttributes();
        } else {
            return productRelatedAttributes.values();
        }
    }

    @Override
    public boolean hasProductRelatedAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().hasProductRelatedAttribute(name);
        } else {
            return productRelatedAttributes.containsKey(name);
        }
    }

    @Override
    public ConsumerAgentProductRelatedAttribute getProductRelatedAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().getProductRelatedAttribute(name);
        } else {
            return productRelatedAttributes.get(name);
        }
    }

    @Override
    public void addProductRelatedAttribute(ConsumerAgentProductRelatedAttribute attribute) {
        if(hasProductRelatedAttribute(attribute)) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' already exists", attribute.getName());
        }
        productRelatedAttributes.put(attribute.getName(), attribute);
    }

    public void addAllProductRelatedAttribute(ConsumerAgentProductRelatedAttribute... attributes) {
        addAllProductRelatedAttribute(Arrays.asList(attributes));
    }

    public void addAllProductRelatedAttribute(Collection<? extends ConsumerAgentProductRelatedAttribute> attributes) {
        for(ConsumerAgentProductRelatedAttribute attr: attributes) {
            addProductRelatedAttribute(attr);
        }
    }

    @Override
    public void updateProductRelatedAttributes(Product newProduct) {
        if(isSynced()) {
            getRealAgent().updateProductRelatedAttributes(newProduct);
        } else {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "update product related attributes in agent '{}' for product '{}'", getName(), newProduct.getName());
            for(ConsumerAgentProductRelatedAttribute relatedAttribute: getProductRelatedAttributes()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "update attribute '{}' in agent '{}' for product '{}'", relatedAttribute.getName(), getName(), newProduct.getName());
                relatedAttribute.getGroup().deriveUpdate(newProduct, relatedAttribute);
            }
        }
    }

    @Override
    public boolean isAware(Product product) {
        if(isSynced()) {
            return getRealAgent().isAware(product);
        } else {
            return awareness.isAware(product);
        }
    }

    @Override
    public void makeAware(Product product) {
        if(isSynced()) {
            getRealAgent().makeAware(product);
        } else {
            awareness.makeAware(product);
        }
    }

    @Override
    public boolean isInterested(Product product) {
        if(isSynced()) {
            return getRealAgent().isInterested(product);
        } else {
            return interest.isInterested(product);
        }
    }

    @Override
    public double getInterest(Product product) {
        if(isSynced()) {
            return getRealAgent().getInterest(product);
        } else {
            return interest.getValue(product);
        }
    }

    @Override
    public void updateInterest(Product product, double value) {
        if(isSynced()) {
            getRealAgent().updateInterest(product, value);
        } else {
            if(value != 0 && !isAware(product)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "'{}' is not aware of '{}'", getName(), product.getName());
            }
            interest.update(product, value);
        }
    }

    @Override
    public void makeInterested(Product product) {
        if(isSynced()) {
            getRealAgent().makeInterested(product);
        } else {
            if(!isAware(product)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "'{}' is not aware of '{}'", getName(), product.getName());
            }
            interest.makeInterested(product);
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
    public ProductInterest getProductInterest() {
        if(isSynced()) {
            return getRealAgent().getProductInterest();
        } else {
            return interest;
        }
    }

    public void setProductInterest(ProductInterest awareness) {
        this.interest = awareness;
    }

    @Override
    public Collection<AdoptedProduct> getAdoptedProducts() {
        if(isSynced()) {
            return getRealAgent().getAdoptedProducts();
        } else {
            return adoptedProducts.values();
        }
    }

    @Override
    public AdoptedProduct getAdoptedProduct(Product product) {
        if(isSynced()) {
            return getRealAgent().getAdoptedProduct(product);
        } else {
            return adoptedProducts.get(product);
        }
    }

    @Override
    public boolean hasAdopted(Product product) {
        if(isSynced()) {
            return getRealAgent().hasAdopted(product);
        } else {
            return adoptedProducts.containsKey(product);
        }
    }

    @Override
    public boolean hasInitialAdopted(Product product) {
        if(isSynced()) {
            return getRealAgent().hasAdopted(product);
        } else {
            AdoptedProduct adoptedProduct = adoptedProducts.get(product);
            return adoptedProduct != null && adoptedProduct.isInitial();
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
            adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
        }
    }

    @Override
    public void adoptInitial(Product product) {
        if(isSynced()) {
            getRealAgent().adoptInitial(product);
        } else {
            BasicAdoptedProduct adoptedProduct = new BasicAdoptedProduct(null, product, null, AdoptionPhase.INITIAL);
            adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
        }
    }

    @Override
    public void adopt(Need need, Product product, Timestamp stamp, AdoptionPhase phase) {
        if(isSynced()) {
            getRealAgent().adopt(need, product, stamp, phase);
        } else {
            BasicAdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, stamp, phase);
            adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
        }
    }

    public void addAllAdoptedProducts(Collection<? extends AdoptedProduct> adoptedProducts) {
        for(AdoptedProduct adoptedProduct: adoptedProducts) {
            this.adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
        }
    }

    @Override
    public ProductFindingScheme getProductFindingScheme() {
        if(isSynced()) {
            return getRealAgent().getProductFindingScheme();
        } else {
            return productFindingScheme;
        }
    }

    public void setProductFindingScheme(ProductFindingScheme productFindingScheme) {
        this.productFindingScheme = productFindingScheme;
    }

    @Override
    public ProcessFindingScheme getProcessFindingScheme() {
        if(isSynced()) {
            return getRealAgent().getProcessFindingScheme();
        } else {
            return processFindingScheme;
        }
    }

    public void setProcessFindingScheme(ProcessFindingScheme processFindingScheme) {
        this.processFindingScheme = processFindingScheme;
    }

    @Override
    public Collection<Need> getNeeds() {
        if(isSynced()) {
            return getRealAgent().getNeeds();
        } else {
            return needs;
        }
    }

    @Override
    public boolean hasNeed(Need need) {
        if(isSynced()) {
            return getRealAgent().hasNeed(need);
        } else {
            return needs.contains(need);
        }
    }

    @Override
    public void addNeed(Need need) {
        if(isSynced()) {
            getRealAgent().addNeed(need);
        } else {
            needs.add(need);
        }
    }

    public void addAllNeeds(Need... needs) {
        this.needs.addAll(Arrays.asList(needs));
    }

    @Override
    public Map<Need, ProcessPlan> getActivePlans() {
        if(isSynced()) {
            return getRealAgent().getActivePlans();
        } else {
            return activePlans;
        }
    }

    @Override
    public Collection<ProcessPlan> getRunningPlans() {
        if(isSynced()) {
            return getRealAgent().getRunningPlans();
        } else {
            return runningPlans;
        }
    }

    public void addAllActivePlans(Map<Need, ProcessPlan> plans) {
        this.activePlans.putAll(plans);
    }

    public void addAllRunningPlans(Collection<ProcessPlan> runningPlans) {
        this.runningPlans.addAll(runningPlans);
    }

    @Override
    public boolean linkAccess(AttributeAccess attributeAccess) {
        if(isSynced()) {
            return getRealAgent().linkAccess(attributeAccess);
        } else {
            return externAttributes.add(attributeAccess);
        }
    }

    @Override
    public boolean unlinkAccess(AttributeAccess attributeAccess) {
        if(isSynced()) {
            return getRealAgent().unlinkAccess(attributeAccess);
        } else {
            return externAttributes.remove(attributeAccess);
        }
    }

    @Override
    public boolean hasAnyAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().hasAnyAttribute(name);
        } else {
            if(hasAttribute(name)) {
                return true;
            }
            for(AttributeAccess attributeAccess: externAttributes) {
                if(attributeAccess.hasAttribute(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Attribute findAttribute(String name) {
        if(isSynced()) {
            return getRealAgent().findAttribute(name);
        } else {
            Attribute attr = getAttribute(name);
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
