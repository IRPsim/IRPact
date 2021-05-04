package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductRelatedAttribute extends ConsumerAgentRelatedAttribute<Product> {

    @Override
    default ConsumerAgentProductRelatedAttribute asRelatedAttribute() {
        return this;
    }
}
