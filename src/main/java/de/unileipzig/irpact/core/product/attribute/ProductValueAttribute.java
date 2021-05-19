package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.attribute.ValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductValueAttribute<V> extends ProductAttribute, ValueAttribute<V> {

    @Override
    ProductValueAttribute<V> asValueAttribute();
}
