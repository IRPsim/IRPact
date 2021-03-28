package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class DoubleAttribute extends AbstractAttribute {

    protected double value;

    public DoubleAttribute() {
    }

    @Override
    public DoubleAttribute copyAttribute() {
        DoubleAttribute copy = new DoubleAttribute();
        copy.setName(getName());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public DataType getType() {
        return DataType.DOUBLE;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        if(value == null) {
            throw new NullPointerException();
        } else {
            this.value = castTo(Number.class, value).doubleValue();
        }
    }

    @Override
    public void setDoubleValue(double value) {
        this.value = value;
    }

    @Override
    public double getDoubleValue() {
        return value;
    }
}
