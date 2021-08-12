package de.unileipzig.irpact.commons.attribute;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ValueAttribute<V> extends Attribute {

    //=========================
    //Value
    //=========================

    boolean isDataType(DataType dataType);

    Collection<DataType> getDataTypes();

    default boolean isNoDataType(DataType dataType) {
        return !isDataType(dataType);
    }

    default <R> R getValue(Class<R> c) {
        return c.cast(getValue());
    }

    @SuppressWarnings("unchecked")
    default <R> R getValueAs() {
        return (R) getValue();
    }

    V getValue();

    void setValue(V value);

    //=========================
    //Attribute
    //=========================

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.VALUE;
    }

    @Override
    ValueAttribute<V> asValueAttribute();

    //=========================
    //special
    //=========================

    default boolean getBooleanValue() {
        throw new UnsupportedOperationException();
    }

    default void setBooleanValue(boolean value) {
        throw new UnsupportedOperationException();
    }

    default int getIntValue() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default void setIntValue(int value) {
        throw new UnsupportedOperationException();
    }

    default long getLongValue() {
        throw new UnsupportedOperationException();
    }

    default void setLongValue(long value) {
        throw new UnsupportedOperationException();
    }

    default double getDoubleValue() {
        throw new UnsupportedOperationException();
    }

    default void setDoubleValue(double value) {
        throw new UnsupportedOperationException();
    }

    default String getStringValue() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default void setStringValue(String value) {
        throw new UnsupportedOperationException();
    }

    default String getValueAsString() {
        throw new UnsupportedOperationException();
    }
}
