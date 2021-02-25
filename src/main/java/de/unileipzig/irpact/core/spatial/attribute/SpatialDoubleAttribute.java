package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialDoubleAttribute extends DoubleAttribute, SpatialAttribute<Number> {

    @Override
    SpatialDoubleAttribute copyAttribute();

    @Override
    default String getValueAsString() {
        return Double.toString(getDoubleValue());
    }
}
