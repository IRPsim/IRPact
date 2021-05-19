package de.unileipzig.irpact.commons.spatial.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicSpatialStringAttribute
        extends AbstractSpatialValueAttribute<String>
        implements SpatialStringAttribute {

    protected String value;

    public BasicSpatialStringAttribute() {
    }

    public BasicSpatialStringAttribute(String name, String value) {
        setName(name);
        setValue(value);
    }

    @Override
    public BasicSpatialStringAttribute copy() {
        BasicSpatialStringAttribute copy = new BasicSpatialStringAttribute();
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setValue(getValue());
        return copy;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setStringValue(String value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                getStringValue()
        );
    }

    @Override
    public BasicSpatialStringAttribute asValueAttribute() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicSpatialStringAttribute)) return false;
        BasicSpatialStringAttribute that = (BasicSpatialStringAttribute) o;
        return artificial == that.artificial
                && Objects.equals(name, that.name)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artificial, name, value);
    }
}
