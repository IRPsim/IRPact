package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.AttributeGroupEntity;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductGroupAttribute extends AttributeGroupEntity<ConsumerAgentGroupProductGroupAttribute> {

    ProductGroup getProductGroup();
}
