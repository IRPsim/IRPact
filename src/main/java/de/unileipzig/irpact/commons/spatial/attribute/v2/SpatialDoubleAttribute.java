package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.DoubleAttribute;
import de.unileipzig.irpact.core.product.attribute.ProductValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialDoubleAttribute extends SpatialValueAttribute<Number>, DoubleAttribute {

    @Override
    SpatialDoubleAttribute asValueAttribute();
}
