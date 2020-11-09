package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.attribute.DerivableUnivariateDistributionAttribute;
import de.unileipzig.irpact.v2.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupProductGroupAttribute extends DerivableUnivariateDistributionAttribute<ConsumerAgentProductGroupAttribute> {

    ProductGroup getProductGroup();
}
