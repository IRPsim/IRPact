package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.data.DataType;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface DoubleAttribute extends ValueAttribute<Number> {

    //=========================
    //Value
    //=========================

    @Override
    default boolean isDataType(DataType dataType) {
        return dataType == DataType.DOUBLE;
    }

    @Override
    default Collection<DataType> getDataTypes() {
        return DataType.SET_DOUBLE;
    }

    @Override
    default void setValue(Number value) {
        setDoubleValue(value.doubleValue());
    }

    @Override
    default Double getValue() {
        return getDoubleValue();
    }

    //=========================
    //Attribute
    //=========================

    @Override
    DoubleAttribute asValueAttribute();

    //=========================
    //special
    //=========================

    @Override
    default boolean getBooleanValue() {
        return getDoubleValue() == 1.0;
    }

    @Override
    default void setBooleanValue(boolean value) {
        setDoubleValue(value ? 1.0 : 0.0);
    }

    @Override
    default int getIntValue() {
        return (int) getDoubleValue();
    }

    @Override
    default void setIntValue(int value) {
        setDoubleValue(value);
    }

    @Override
    default String getValueAsString() {
        return Double.toString(getDoubleValue());
    }
}
