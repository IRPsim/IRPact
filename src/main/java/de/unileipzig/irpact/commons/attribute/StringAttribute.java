package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.DataType;

/**
 * @author Daniel Abitz
 */
public interface StringAttribute extends Attribute<String> {

    @Override
    default DataType getType() {
        return DataType.STRING;
    }

    default String getValue() {
        return getStringValue();
    }

    default void setValue(String value) {
        setStringValue(value);
    }

    String getStringValue();

    void setStringValue(String value);
}
