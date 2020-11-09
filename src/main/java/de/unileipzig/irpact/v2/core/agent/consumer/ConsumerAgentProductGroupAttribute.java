package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.attribute.DoubleAttributeGroupEntity;
import de.unileipzig.irpact.v2.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductGroupAttribute extends DoubleAttributeGroupEntity<ConsumerAgentGroupProductGroupAttribute> {

    ProductGroup getProductGroup();
}
