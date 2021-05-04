package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
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
                getDoubleValue()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BasicSpatialStringAttribute asValueAttribute() {
        return this;
    }
}
