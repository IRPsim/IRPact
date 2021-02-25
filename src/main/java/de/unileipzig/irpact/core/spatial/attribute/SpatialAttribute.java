package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.Attribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialAttribute<T> extends Attribute<T> {

    @Override
    SpatialAttribute<T> copyAttribute();

    String getValueAsString();
}
