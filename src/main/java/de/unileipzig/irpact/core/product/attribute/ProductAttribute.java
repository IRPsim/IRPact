package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.attribute.v3.GroupEntityAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductAttribute extends GroupEntityAttribute<ProductGroupAttribute> {

    @Override
    ProductAttribute copy();

    @Override
    ProductValueAttribute<?> asValueAttribute();
}
