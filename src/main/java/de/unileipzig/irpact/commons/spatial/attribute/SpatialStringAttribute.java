package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.StringAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class SpatialStringAttribute extends StringAttribute implements SpatialAttribute {

    public SpatialStringAttribute() {
    }

    public SpatialStringAttribute(String name, String value) {
        setName(name);
        setStringValue(value);
    }

    @Override
    public SpatialStringAttribute copyAttribute() {
        SpatialStringAttribute copy = new SpatialStringAttribute();
        copy.setName(getName());
        copy.setStringValue(getStringValue());
        return copy;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(getName(), getStringValue());
    }
}
