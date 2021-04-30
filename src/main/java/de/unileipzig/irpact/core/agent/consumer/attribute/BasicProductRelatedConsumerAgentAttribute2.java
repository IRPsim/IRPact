package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AbstractGroupEntityRelatedAttribute;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class BasicProductRelatedConsumerAgentAttribute2
        extends AbstractGroupEntityRelatedAttribute<Product, ConsumerAgentGroupAttribute>
        implements ProductRelatedConsumerAgentAttribute2 {

    public BasicProductRelatedConsumerAgentAttribute2() {
        super(MapSupplier.getDefault());
    }

    public BasicProductRelatedConsumerAgentAttribute2(String name, ProductRelatedConsumerAgentGroupAttribute2 groupAttribute, boolean artificial) {
        setName(name);
        setGroup(groupAttribute);
        setArtificial(artificial);
    }
}
