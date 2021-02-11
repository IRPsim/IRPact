package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.misc.Placeholder;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.jadex.agents.consumer.ConsumerAgentInitializationData;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class PlaceholderConsumerAgent
        extends SpatialInformationAgentBase
        implements ConsumerAgent, ConsumerAgentInitializationData, Placeholder {

    protected ConsumerAgentGroup group;
    protected SocialGraph.Node node;
    protected Map<String, ConsumerAgentAttribute> attributes;
    protected Awareness<Product> awareness;
    protected Set<AdoptedProduct> adoptedProducts;
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;

    public PlaceholderConsumerAgent() {
        this(new HashMap<>(), new HashSet<>());
    }

    public PlaceholderConsumerAgent(Map<String, ConsumerAgentAttribute> attributes, Set<AdoptedProduct> adoptedProducts) {
        this.attributes = attributes;
        this.adoptedProducts = adoptedProducts;
    }

    protected static UnsupportedOperationException placeholderException() {
        throw placeholderException();
    }

    @Override
    public boolean aquireAction() {
        throw placeholderException();
    }

    @Override
    public SocialGraph.Node getSocialGraphNode() {
        return node;
    }

    @Override
    public void setSocialGraphNode(SocialGraph.Node node) {
        this.node = node;
    }

    @Override
    public ConsumerAgentGroup getGroup() {
        return group;
    }

    public void setGroup(ConsumerAgentGroup group) {
        this.group = group;
    }

    @Override
    public Collection<ConsumerAgentAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public ConsumerAgentAttribute getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean addAttributes(Collection<? extends ConsumerAgentAttribute> attributes) {
        boolean changed = false;
        for(ConsumerAgentAttribute cagAttr: attributes) {
            changed |= addAttribute(cagAttr);
        }
        return changed;
    }

    @Override
    public boolean addAttribute(ConsumerAgentAttribute attribute) {
        if(attributes.containsKey(attribute.getName())) {
            return false;
        } else {
            attributes.put(attribute.getName(), attribute);
            return true;
        }
    }

    @Override
    public Awareness<Product> getProductAwareness() {
        return awareness;
    }

    public void setProductAwareness(Awareness<Product> awareness) {
        this.awareness = awareness;
    }

    @Override
    public Collection<AdoptedProduct> getAdoptedProducts() {
        return adoptedProducts;
    }

    @Override
    public boolean hasAdopted(Product product) {
        for(AdoptedProduct ap: adoptedProducts) {
            if(ap.getProduct() == product) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean adopt(Need need, Product product) {
        throw placeholderException();
    }

    @Override
    public ProductFindingScheme getProductFindingScheme() {
        return productFindingScheme;
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
    public PlaceholderConsumerAgent getPlaceholder() {
        return this;
    }
}
