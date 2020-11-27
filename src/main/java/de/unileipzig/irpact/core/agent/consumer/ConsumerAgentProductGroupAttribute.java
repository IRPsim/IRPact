package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.DoubleAttributeGroupEntity;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductGroupAttribute extends DoubleAttributeGroupEntity<ConsumerAgentGroupProductGroupAttribute> {

    ProductGroup getProductGroup();
}
