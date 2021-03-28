package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distributionattribut.DerivableUnivariateDoubleDistributionAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupProductGroupAttribute extends DerivableUnivariateDoubleDistributionAttribute<ConsumerAgentProductGroupAttribute> {

    ProductGroup getProductGroup();
}
