package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.DataType;

/**
 * @author Daniel Abitz
 */
public interface DoubleAttribute extends Attribute<Number> {

    @Override
    DoubleAttribute copyAttribute();

    @Override
    default DataType getType() {
        return DataType.DOUBLE;
    }

    default Double getValue() {
        return getDoubleValue();
    }

    default void setValue(Number value) {
        setDoubleValue(value.doubleValue());
    }

    double getDoubleValue();

    void setDoubleValue(double value);
}
