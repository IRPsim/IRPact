package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public class BasicDoubleAttribute2 extends AbstractNumberAttribute2 implements DoubleAttribute2 {

    protected double value;

    public BasicDoubleAttribute2() {
        this(null, 0);
    }

    public BasicDoubleAttribute2(String name) {
        this(name, 0);
    }

    public BasicDoubleAttribute2(double value) {
        this(null, value);
    }

    public BasicDoubleAttribute2(String name, double value) {
        setName(name);
        setDouble(value);
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public void setDouble(double value) {
        this.value = value;
    }

    @Override
    public BasicDoubleAttribute2 copy() {
        return new BasicDoubleAttribute2(getName(), getDouble());
    }
}
