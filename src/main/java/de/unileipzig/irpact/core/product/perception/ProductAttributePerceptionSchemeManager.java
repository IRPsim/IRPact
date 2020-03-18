package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.core.product.ProductAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductAttributePerceptionSchemeManager {

    ProductAttributePerceptionScheme getScheme(ProductAttribute attribute);
}
