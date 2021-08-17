package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.ValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialValueAttribute<V> extends SpatialAttribute, ValueAttribute<V> {

    @Override
    SpatialValueAttribute<V> copy();

    @Override
    SpatialValueAttribute<V> asValueAttribute();
}
