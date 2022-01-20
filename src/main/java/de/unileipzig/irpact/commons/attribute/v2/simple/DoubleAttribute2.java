package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public interface DoubleAttribute2 extends NumberAttribute2 {

    @Override
    DoubleAttribute2 copy();

    @Override
    default boolean isDouble() {
        return true;
    }
    @Override
    default DoubleAttribute2 asDouble() {
        return this;
    }

    @Override
    default AttributeType2 getType() {
        return AttributeType2.DOUBLE;
    }

    @Override
    default Double getValue() {
        return getDouble();
    }
    @Override
    default void setValue(Number value) {
        setDouble(value.doubleValue());
    }

    @Override
    default boolean getBoolean() {
        return getDouble() == 1.0;
    }
    @Override
    default void setBoolean(boolean value) {
        setDouble(value ? 1.0 : 0.0);
    }

    @Override
    default int getInt() {
        return (int) getDouble();
    }
    @Override
    default void setInt(int value) {
        setDouble(value);
    }

    @Override
    default long getLong() {
        return (long) getDouble();
    }
    @Override
    default void setLong(long value) {
        setDouble(value);
    }
}
