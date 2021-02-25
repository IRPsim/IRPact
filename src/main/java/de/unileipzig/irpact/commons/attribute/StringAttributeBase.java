package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class StringAttributeBase extends NameableBase implements StringAttribute {

    protected String value;

    public StringAttributeBase() {
    }

    @Override
    public StringAttributeBase copyAttribute() {
        StringAttributeBase copy = new StringAttributeBase();
        copy.setName(getName());
        copy.setStringValue(getStringValue());
        return copy;
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
