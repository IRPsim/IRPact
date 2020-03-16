package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.attribute.Attribute;

/**
 * @author Daniel Abitz
 */
public interface ProductAttribute extends Attribute {

    ProductGroupAttribute getGroup();
}
