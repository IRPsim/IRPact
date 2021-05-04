package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialDoubleAttribute extends SpatialValueAttribute<Number>, DoubleAttribute {

    @Override
    SpatialDoubleAttribute asValueAttribute();
}
