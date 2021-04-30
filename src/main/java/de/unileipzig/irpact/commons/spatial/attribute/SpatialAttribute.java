package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.ValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface SpatialAttribute extends ValueAttribute {

    @Override
    SpatialAttribute copy();
}
