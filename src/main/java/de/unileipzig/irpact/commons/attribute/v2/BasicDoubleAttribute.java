package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class BasicDoubleAttribute extends AbstractValueAttribute {

    protected double value;

    public BasicDoubleAttribute() {
    }

    public BasicDoubleAttribute(String name, double value) {
        setName(name);
        setDoubleValue(value);
    }

    @Override
    public BasicDoubleAttribute copy() {
        BasicDoubleAttribute copy = new BasicDoubleAttribute();
        copy.setName(getName());
        copy.setDoubleValue(getDoubleValue());
        return copy;
    }

    @Override
    public DataType getDataType() {
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
