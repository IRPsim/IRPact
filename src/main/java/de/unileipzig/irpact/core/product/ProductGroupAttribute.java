package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.attribute.UnivariateAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductGroupAttribute extends UnivariateAttribute {

    ProductAttribute derive();

    ProductAttribute derive(double fixedValue);
}
