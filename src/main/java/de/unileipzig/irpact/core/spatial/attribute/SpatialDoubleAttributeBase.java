package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.DoubleAttributeBase;

/**
 * @author Daniel Abitz
 */
public class SpatialDoubleAttributeBase extends DoubleAttributeBase implements SpatialDoubleAttribute {

    public SpatialDoubleAttributeBase() {
    }

    public SpatialDoubleAttributeBase(String name, double value) {
        setName(name);
        setDoubleValue(value);
    }

    @Override
    public SpatialDoubleAttributeBase copyAttribute() {
        SpatialDoubleAttributeBase copy = new SpatialDoubleAttributeBase();
        copy.setName(getName());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public String toString() {
        return "{" + getName() + "=" + getValueAsString() + "}";
    }
}
