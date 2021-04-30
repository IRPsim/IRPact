package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.attribute.DoubleAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class SpatialDoubleAttribute extends DoubleAttribute implements SpatialAttribute {

    public SpatialDoubleAttribute() {
    }

    public SpatialDoubleAttribute(String name, double value) {
        setName(name);
        setDoubleValue(value);
    }

    @Override
    public SpatialDoubleAttribute copy() {
        SpatialDoubleAttribute copy = new SpatialDoubleAttribute();
        copy.setName(getName());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(getName(), getDoubleValue());
    }
}
