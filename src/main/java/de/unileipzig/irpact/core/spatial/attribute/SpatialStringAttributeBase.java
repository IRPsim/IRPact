package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.StringAttributeBase;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class SpatialStringAttributeBase extends StringAttributeBase implements SpatialStringAttribute {

    public SpatialStringAttributeBase() {
    }

    public SpatialStringAttributeBase(String name, String value) {
        setName(name);
        setStringValue(value);
    }

    @Override
    public SpatialStringAttributeBase copyAttribute() {
        SpatialStringAttributeBase copy = new SpatialStringAttributeBase();
        copy.setName(getName());
        copy.setStringValue(getStringValue());
        return copy;
    }

    @Override
    public String toString() {
        return "{" + getName() + "=" + getValueAsString() + "}";
    }

    @Override
    public int getHashCode() {
        return Objects.hash(getName(), getStringValue());
    }
}
