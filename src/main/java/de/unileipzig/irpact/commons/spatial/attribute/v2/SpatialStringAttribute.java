package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.StringAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialStringAttribute extends SpatialAttribute<String>, StringAttribute {

    @Override
    SpatialStringAttribute asValueAttribute();
}
