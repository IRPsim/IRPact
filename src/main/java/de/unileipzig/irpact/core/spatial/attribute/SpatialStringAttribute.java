package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.StringAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialStringAttribute extends StringAttribute, SpatialAttribute<String> {

    @Override
    SpatialStringAttribute copyAttribute();

    @Override
    default String getValueAsString() {
        return getStringValue();
    }
}
