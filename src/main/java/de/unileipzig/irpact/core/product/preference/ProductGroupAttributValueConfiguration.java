package de.unileipzig.irpact.core.product.preference;

import de.unileipzig.irpact.core.preference.Value;
import de.unileipzig.irpact.core.preference.ValueConfiguration;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class ProductGroupAttributValueConfiguration
        extends ValueConfiguration<ProductGroupAttribute, ProductGroupAttributValueMapping> {

    public ProductGroupAttributValueConfiguration(
            Map<String, Value> values,
            Set<ProductGroupAttributValueMapping> valueMappings) {
        super(values, valueMappings);
    }
}
