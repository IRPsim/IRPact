package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.GroupAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProductRelatedConsumerAgentGroupAttribute extends GroupAttribute {

    Collection<ProductGroup> getProductGroups();

    boolean hasAttribute(ProductGroup productGroup);

    ConsumerAgentGroupAttribute getAttribute(ProductGroup productGroup);

    void set(ProductGroup productGroup, ConsumerAgentGroupAttribute groupAttribute);

    ConsumerAgentAttribute derive(Product product);
}
