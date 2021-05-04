package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.Attribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialAttribute extends Attribute {

    @Override
    SpatialAttribute copy();

    @Override
    SpatialValueAttribute<?> asValueAttribute();
}
