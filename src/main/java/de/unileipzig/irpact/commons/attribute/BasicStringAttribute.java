package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public class BasicStringAttribute extends AbstractAttribute implements StringAttribute {

    protected String value;

    public BasicStringAttribute() {
    }

    public BasicStringAttribute(String value) {
        setStringValue(value);
    }

    public BasicStringAttribute(String name, String value) {
        setName(name);
        setStringValue(value);
    }

    @Override
    public BasicStringAttribute asValueAttribute() {
        return this;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public void setStringValue(String value) {
        this.value = value;
    }
}
