package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductRelatedGroupAttribute extends ConsumerAgentRelatedGroupAttribute<Product, Product> {

    void derive(Product input, ConsumerAgentProductRelatedAttribute target);
}
