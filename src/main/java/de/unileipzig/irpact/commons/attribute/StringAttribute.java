package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.data.DataType;

import java.util.Collection;

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
    default Collection<DataType> getDataTypes() {
        return DataType.SET_STRING;
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
