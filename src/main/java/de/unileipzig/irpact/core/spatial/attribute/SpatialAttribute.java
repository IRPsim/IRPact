package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.Attribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialAttribute extends Attribute {

    @Override
    SpatialAttribute copyAttribute();

    String getValueAsString();
}
