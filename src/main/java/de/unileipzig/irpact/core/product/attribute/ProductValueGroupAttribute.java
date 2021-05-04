package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.DirectDerivable;

/**
 * @author Daniel Abitz
 */
public interface ProductValueGroupAttribute<V> extends ProductGroupAttribute, DirectDerivable<ProductValueAttribute<V>> {
}
