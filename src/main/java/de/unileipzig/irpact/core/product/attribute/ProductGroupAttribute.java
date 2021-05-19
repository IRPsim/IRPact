package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.derivable.DirectDerivable;
import de.unileipzig.irpact.commons.attribute.GroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductGroupAttribute extends GroupAttribute, DirectDerivable<ProductAttribute> {

    @Override
    ProductGroupAttribute copy();

    default boolean isProductDoubleGroupAttribute() {
        return false;
    }

    default ProductDoubleGroupAttribute asProductDoubleGroupAttribute() {
        throw new UnsupportedOperationException();
    }
}
