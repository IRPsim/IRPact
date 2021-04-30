package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.ValueAttribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentValueAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ProductRelatedConsumerAgentAttribute;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.product.interest.ProductInterest;

import java.util.Collection;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgent extends SpatialInformationAgent {

    ConsumerAgentGroup getGroup();

    Collection<ConsumerAgentValueAttribute> getAttributes();

    ConsumerAgentValueAttribute getAttribute(String name);

    boolean hasAttribute(String name);

    void addAttribute(ConsumerAgentValueAttribute attribute);

    Collection<ProductRelatedConsumerAgentAttribute> getProductRelatedAttributes();

    boolean hasProductRelatedAttribute(String name);

    default boolean hasProductRelatedAttribute(ProductRelatedConsumerAgentAttribute attribute) {
        return hasProductRelatedAttribute(attribute.getName());
    }

    ProductRelatedConsumerAgentAttribute getProductRelatedAttribute(String name);

    void addProductRelatedAttribute(ProductRelatedConsumerAgentAttribute attribute);

    void updateProductRelatedAttributes(Product newProduct);

    boolean isAware(Product product);

    void makeAware(Product product);

    ProductAwareness getProductAwareness();

    boolean isInterested(Product product);

    double getInterest(Product product);

    void updateInterest(Product product, double value);

    void makeInterested(Product product);

    ProductInterest getProductInterest();

    Collection<AdoptedProduct> getAdoptedProducts();

    boolean hasAdopted(Product product);

    void addAdoptedProduct(AdoptedProduct adoptedProduct);

    void adoptInitial(Product product);

    void adopt(Need need, Product product, Timestamp stamp);

    boolean linkAccess(AttributeAccess attributeAccess);

    boolean unlinkAccess(AttributeAccess attributeAccess);

    boolean hasAnyAttribute(String name);

    ValueAttribute findAttribute(String name);

    Collection<Need> getNeeds();

    boolean hasNeed(Need need);

    void addNeed(Need need);

    ProductFindingScheme getProductFindingScheme();

    ProcessFindingScheme getProcessFindingScheme();

    Map<Need, ProcessPlan> getPlans();
}
