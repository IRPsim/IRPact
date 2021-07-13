package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public class BasicDoubleAttribute extends AbstractAttribute implements DoubleAttribute {

    protected double value;

    public BasicDoubleAttribute() {
    }

    public BasicDoubleAttribute(double value) {
        setDoubleValue(value);
    }

    public BasicDoubleAttribute(String name, double value) {
        setName(name);
        setDoubleValue(value);
    }

    @Override
    public BasicDoubleAttribute asValueAttribute() {
        return this;
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
