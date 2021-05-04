package de.unileipzig.irpact.commons.attribute.v3;

import de.unileipzig.irpact.commons.util.data.DataType;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface StringAttribute extends ValueAttribute<String> {

    //=========================
    //Value
    //=========================

    @Override
    default boolean isDataType(DataType dataType) {
        return dataType == DataType.STRING;
    }

    @Override
    default void setValue(String value) {
        setStringValue(value);
    }

    @Override
    default String getValue() {
        return getStringValue();
    }

    //=========================
    //Attribute
    //=========================

    @Override
    StringAttribute asValueAttribute();

    //=========================
    //special
    //=========================

    @Override
    String getStringValue();

    @Override
    void setStringValue(String value);

    @Override
    default String getValueAsString() {
        return getStringValue();
    }
}
