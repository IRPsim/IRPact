package de.unileipzig.irpact.core.product.preference;

import de.unileipzig.irpact.core.preference.Value;
import de.unileipzig.irpact.core.preference.ValueMapping;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

/**
 * @author Daniel Abitz
 */
public class ProductGroupAttributValueMapping extends ValueMapping<ProductGroupAttribute> {

    public ProductGroupAttributValueMapping(
            ProductGroupAttribute object,
            Value value,
            double strength) {
        super(object, value, strength);
    }
}
