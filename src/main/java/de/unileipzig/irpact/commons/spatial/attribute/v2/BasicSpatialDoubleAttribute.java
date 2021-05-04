package de.unileipzig.irpact.commons.spatial.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicSpatialDoubleAttribute
        extends AbstractSpatialAttribute<Number>
        implements SpatialDoubleAttribute {

    protected double value;

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

    @SuppressWarnings("unchecked")
    @Override
    public SpatialDoubleAttribute asValueAttribute() {
        return this;
    }
}
