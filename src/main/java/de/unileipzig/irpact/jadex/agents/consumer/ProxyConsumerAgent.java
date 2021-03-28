package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.ProxyAgent;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.Reference;

import java.util.*;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class ProxyConsumerAgent extends SpatialInformationAgentBase implements ConsumerAgent, ProxyAgent<ConsumerAgent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProxyConsumerAgent.class);

    protected ConsumerAgent realAgent;

    protected ConsumerAgentGroup group;
    protected SocialGraph.Node node;
    protected Map<String, ConsumerAgentAttribute> attributes;
    protected ProductInterest interest;
    protected Set<AdoptedProduct> adoptedProducts;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;
    protected Set<Need> needs;
    protected Map<Need, ProcessPlan> plans;
    protected Set<AttributeAccess> externAttributes;

    public ProxyConsumerAgent() {
        this(
                new LinkedHashMap<>(),
                new LinkedHashSet<>(),
                new LinkedHashSet<>(),
                new LinkedHashMap<>(),
                new LinkedHashSet<>()
        );
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
    public int getChecksum() {
        if(isSynced()) {
            return getRealAgent().getChecksum();
        } else {
            return Objects.hash(
                    getName(),
                    getGroup().getName(),
                    getInformationAuthority(),
                    getSpatialInformation().getChecksum(),
                    ChecksumComparable.getCollChecksum(getAttributes()),
                    getProductInterest().getChecksum(),
                    ChecksumComparable.getCollChecksum(getAdoptedProducts()),
                    getProductFindingScheme().getChecksum(),
                    getProcessFindingScheme().getChecksum(),
                    ChecksumComparable.getCollChecksum(getNeeds()),
                    ChecksumComparable.getMapChecksum(getPlans()),
                    ChecksumComparable.getCollChecksum(getExternAttributes())
            );
        }
    }

    private static void logHash(String msg, int storedHash) {
        LOGGER.warn(
                "hash @ '{}': stored={}",
                msg,
                Integer.toHexString(storedHash)
        );
    }

    public void deepHashCheck() {
        logHash("name", ChecksumComparable.getChecksum(getName()));
        logHash("group name", ChecksumComparable.getChecksum(getGroup().getName()));
        logHash("information authority", ChecksumComparable.getChecksum(getInformationAuthority()));
        logHash("spatial information", ChecksumComparable.getChecksum(getSpatialInformation()));
        logHash("attributes", ChecksumComparable.getCollChecksum(getAttributes()));
        logHash("interest", ChecksumComparable.getChecksum(getProductInterest()));
        logHash("adopted products", ChecksumComparable.getCollChecksum(getAdoptedProducts()));
        logHash("product finding scheme", ChecksumComparable.getChecksum(getProductFindingScheme()));
        logHash("process finding scheme", ChecksumComparable.getChecksum(getProcessFindingScheme()));
        logHash("needs", ChecksumComparable.getCollChecksum(getNeeds()));
        logHash("plans", ChecksumComparable.getMapChecksum(getPlans()));
        logHash("external attributes", ChecksumComparable.getCollChecksum(getExternAttributes()));
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
        interest = null;
        adoptedProducts.clear();
        productFindingScheme = null;
        processFindingScheme = null;
        needs.clear();
        plans.clear();
    }

    public void unsync(ConsumerAgent realAgent) {
        if(isNotSynced()) {
            throw new IllegalStateException("not synced");
        }
        if(this.realAgent != realAgent) {
            throw new IllegalArgumentException("synced to another agent");
        }
        this.realAgent = null;
        reset(realAgent);
    }

    protected void reset(ConsumerAgent realAgent) {
        group = realAgent.getGroup();
        node = realAgent.getSocialGraphNode();
        addAllAttributes(realAgent.getAttributes());
        interest = realAgent.getProductInterest();
        adoptedProducts.addAll(realAgent.getAdoptedProducts());
        productFindingScheme = realAgent.getProductFindingScheme();
        processFindingScheme = realAgent.getProcessFindingScheme();
        needs.addAll(realAgent.getNeeds());
        addAllPlans(realAgent.getPlans());
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
    public boolean tryAquireAction() {
        checkSynced();
        return getRealAgent().tryAquireAction();
    }

    @Override
    public boolean tryAquireSelf() {
        checkSynced();
        return getRealAgent().tryAquireSelf();
    }

    @Override
    public void allowAquire() {
        checkSynced();
        getRealAgent().allowAquire();
    }

    @Override
    public void aquireFailed() {
        checkSynced();
        getRealAgent().aquireFailed();
    }

    @Override
    public void actionPerformed() {
        checkSynced();
        getRealAgent().actionPerformed();
    }

    @Override
    public void releaseAquire() {
        checkSynced();
        getRealAgent().releaseAquire();
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
    public ProductInterest getProductInterest() {
        if(isSynced()) {
            return getRealAgent().getProductInterest();
        } else {
            return interest;
        }
    }

    public void setProductAwareness(ProductInterest awareness) {
        this.interest = awareness;
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
        if(isSynced()) {
            getRealAgent().adopt(need, product);
        } else {
            Timestamp now = environment.getTimeModel().now();
            BasicAdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, now);
            adoptedProducts.add(adoptedProduct);
        }
    }

    @Override
    public void adoptAt(Need need, Product product, Timestamp stamp) {
        if(isSynced()) {
            getRealAgent().adoptAt(need, product, stamp);
        } else {
            BasicAdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, stamp);
            adoptedProducts.add(adoptedProduct);
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
        return processFindingScheme;
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
    public Map<Need, ProcessPlan> getPlans() {
        if(isSynced()) {
            return getRealAgent().getPlans();
        } else {
            return plans;
        }
    }

    public void addAllPlans(Map<Need, ProcessPlan> plans) {
        this.plans.putAll(plans);
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
    public Attribute findAttribute(String name) {
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
