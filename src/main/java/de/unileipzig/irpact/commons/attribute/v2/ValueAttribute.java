package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.util.data.DataType;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface ValueAttribute extends Attribute {

    //=========================
    //Value
    //=========================

    DataType getDataType();

    Object getValue();

    default <R> R getValue(Class<R> c) {
        return c.cast(getValue());
    }

    @SuppressWarnings("unchecked")
    default <R> R getValueAs() {
        return (R) getValue();
    }

    void setValue(Object value);

    //=========================
    //Attribute
    //=========================

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.VALUE;
    }

    @Override
    default ValueAttribute asValueAttribute() {
        return this;
    }

    //=========================
    //special
    //=========================

    default boolean getDoubleValueAsBoolean() {
        return getDoubleValue() == 1.0;
    }

    default void setDoubleValue(boolean value) {
        setDoubleValue(value ? 1.0 : 0.0);
    }

    default int getIntValue() {
        return (int) getDoubleValue();
    }

    default void setIntValue(int value) {
        setDoubleValue(value);
    }

    default double getDoubleValue() {
        throw new UnsupportedOperationException();
    }

    default void setDoubleValue(double value) {
        throw new UnsupportedOperationException();
    }

    default String getValueAsString() {
        if(getDataType() == DataType.DOUBLE) {
            return Double.toString(getDoubleValue());
        } else {
            return Objects.toString(getValue());
        }
    }
}
