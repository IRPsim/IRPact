package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface DoubleAttribute extends Attribute<Number> {

    default Double getValue() {
        return getDoubleValue();
    }

    default void setValue(Number value) {
        setDoubleValue(value.doubleValue());
    }

    double getDoubleValue();

    void setDoubleValue(double value);
}
