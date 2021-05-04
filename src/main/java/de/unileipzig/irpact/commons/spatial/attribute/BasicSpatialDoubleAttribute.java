package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public class BasicSpatialDoubleAttribute
        extends AbstractSpatialValueAttribute<Number>
        implements SpatialDoubleAttribute {

    protected double value;

    public BasicSpatialDoubleAttribute() {
    }

    public BasicSpatialDoubleAttribute(String name, double value) {
        setName(name);
        setDoubleValue(value);
    }

    @Override
    public BasicSpatialDoubleAttribute copy() {
        BasicSpatialDoubleAttribute copy = new BasicSpatialDoubleAttribute();
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public double getDoubleValue() {
        return value;
    }

    @Override
    public void setDoubleValue(double value) {
        this.value = value;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                getDoubleValue()
        );
    }

    @Override
    public SpatialDoubleAttribute asValueAttribute() {
        return this;
    }
}
