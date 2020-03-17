package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.preference.ValueConfiguration;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicSimulationConfig implements SimulationConfig {

    protected ConsumerAgentGroupAffinitiesMapping affinitiesMapping;
    protected ValueConfiguration<ProductGroupAttribute> productValues;

    public BasicSimulationConfig() {
    }

    public void setAffinitiesMapping(ConsumerAgentGroupAffinitiesMapping affinitiesMapping) {
        this.affinitiesMapping = affinitiesMapping;
    }

    public void setProductValues(ValueConfiguration<ProductGroupAttribute> productValues) {
        this.productValues = productValues;
    }

    @Override
    public ConsumerAgentGroupAffinitiesMapping getAffinitiesMapping() {
        return affinitiesMapping;
    }

    @Override
    public ValueConfiguration<ProductGroupAttribute> getProductValues() {
        return productValues;
    }
}
