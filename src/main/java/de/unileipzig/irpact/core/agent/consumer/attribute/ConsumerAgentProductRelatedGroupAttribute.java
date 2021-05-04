package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductRelatedGroupAttribute extends ConsumerAgentRelatedGroupAttribute<Product, Product> {

    @Override
    default boolean isProductRelatedGroupAttribute() {
        return true;
    }

    @Override
    default ConsumerAgentProductRelatedGroupAttribute asProductRelatedGroupAttribute() {
        return this;
    }

    void deriveUpdate(Product input, ConsumerAgentProductRelatedAttribute target);

    @Override
    ConsumerAgentProductRelatedAttribute derive();

    @Override
    ConsumerAgentProductRelatedAttribute derive(Product input);

    boolean hasAttribute(ProductGroup productGroup);

    ConsumerAgentGroupAttribute getAttribute(ProductGroup productGroup);
}
