package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.GroupEntityValueAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentProductGroupAttribute extends GroupEntityValueAttribute<ConsumerAgentGroupProductGroupAttribute> {

    ProductGroup getProductGroup();
}
