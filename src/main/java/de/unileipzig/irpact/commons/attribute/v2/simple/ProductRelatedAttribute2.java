package de.unileipzig.irpact.commons.attribute.v2.simple;

import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProductRelatedAttribute2 extends MapContainerAttribute2<Product> {

    @Override
    ProductRelatedAttribute2 copy();

    @Override
    default AttributeType2 getType() {
        return AttributeType2.PRODUCT;
    }

    @Override
    default boolean isProduct() {
        return true;
    }
    @Override
    default ProductRelatedAttribute2 asProduct() {
        return this;
    }
}
