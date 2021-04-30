package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class StringAttribute extends AbstractValueAttribute {

    protected String value;

    @Override
    public StringAttribute copy() {
        StringAttribute copy = new StringAttribute();
        copy.setName(getName());
        copy.setStringValue(getStringValue());
        return copy;
    }

    @Override
    public DataType getDataType() {
        return DataType.STRING;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = castTo(String.class, value);
    }

    @Override
    public void setStringValue(String value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
