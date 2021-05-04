package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicProductDoubleAttribute
        extends AbstractProductAttribute
        implements ProductDoubleAttribute {

    protected double value;

    @Override
    public BasicProductDoubleAttribute copy() {
        BasicProductDoubleAttribute copy = new BasicProductDoubleAttribute();
        copy.setGroup(getGroup());
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
                ChecksumComparable.getNameChecksum(getGroup()),
                getDoubleValue()
        );
    }
}
