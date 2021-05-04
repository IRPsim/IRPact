package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.DirectDerivable;
import de.unileipzig.irpact.commons.attribute.v3.GroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductGroupAttribute extends GroupAttribute, DirectDerivable<ProductAttribute> {

    @Override
    ProductGroupAttribute copy();
}
