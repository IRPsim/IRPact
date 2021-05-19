package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicSpatialDoubleAttribute)) return false;
        BasicSpatialDoubleAttribute that = (BasicSpatialDoubleAttribute) o;
        return artificial == that.artificial
                && Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artificial, name, value);
    }
}
