package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.GroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentValueAttribute;
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

    ConsumerAgentValueAttribute derive(Product product);
}
