package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class BasicStringAttribute extends AbstractGenericValueAttribute<String> {

    public BasicStringAttribute() {
    }

    public BasicStringAttribute(String name, String value) {
        setName(name);
        setStringValue(value);
    }

    @Override
    public BasicStringAttribute copy() {
        BasicStringAttribute copy = new BasicStringAttribute();
        copy.setName(getName());
        copy.setStringValue(getStringValue());
        return copy;
    }

    @Override
    public DataType getDataType() {
        return DataType.STRING;
    }

    @Override
    protected Class<String> getTClass() {
        return String.class;
    }

    public void setStringValue(String value) {
        this.value = value;
    }

    public String getStringValue() {
        return value;
    }
}
