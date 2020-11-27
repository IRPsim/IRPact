package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.DerivableUnivariateDistributionAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupProductGroupAttribute extends DerivableUnivariateDistributionAttribute<ConsumerAgentProductGroupAttribute> {

    ProductGroup getProductGroup();
}