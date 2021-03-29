package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.GroupEntity;
import de.unileipzig.irpact.commons.attribute.AttributeBase;
import de.unileipzig.irpact.core.product.Product;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductRelatedConsumerAgentAttribute extends AttributeBase, GroupEntity<ProductRelatedConsumerAgentGroupAttribute> {

    Collection<Product> getProducts();

    boolean hasAttribute(Product product);

    ConsumerAgentAttribute getAttribute(Product product);

    void set(Product product, ConsumerAgentAttribute attribute);
}
