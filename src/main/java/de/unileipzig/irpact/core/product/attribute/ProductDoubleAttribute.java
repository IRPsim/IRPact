package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.attribute.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductDoubleAttribute extends ProductValueAttribute<Number>, DoubleAttribute {

    @Override
    default ProductDoubleAttribute asValueAttribute() {
        return this;
    }
}
