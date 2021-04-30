package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.distributionattribut.DerivableUnivariateDoubleDistributionAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductGroupAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupProductGroupAttribute extends DerivableUnivariateDoubleDistributionAttribute<ConsumerAgentProductGroupAttribute> {

    ProductGroup getProductGroup();
}
