package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.ValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialAttribute<V> extends ValueAttribute<V> {

    @Override
    SpatialAttribute<V> copy();

    @Override
    SpatialAttribute<V> asValueAttribute();
}
