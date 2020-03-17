package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.preference.ValueConfiguration;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface SimulationConfig {

    ConsumerAgentGroupAffinitiesMapping getAffinitiesMapping();

    ValueConfiguration<ProductGroupAttribute> getProductValues();
}
