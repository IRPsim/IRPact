package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.ValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialValueAttribute<V> extends SpatialAttribute, ValueAttribute<V> {

    @Override
    SpatialValueAttribute<V> copy();

    @Override
    SpatialValueAttribute<V> asValueAttribute();
}
