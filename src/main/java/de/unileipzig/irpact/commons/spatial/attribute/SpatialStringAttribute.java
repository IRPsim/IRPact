package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.StringAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialStringAttribute extends SpatialValueAttribute<String>, StringAttribute {

    @Override
    SpatialStringAttribute asValueAttribute();
}
