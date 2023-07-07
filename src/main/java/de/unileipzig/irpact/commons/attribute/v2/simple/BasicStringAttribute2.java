package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public class BasicStringAttribute2 extends AbstractValueAttribute2<String> implements StringAttribute2 {

    protected String value;

    public BasicStringAttribute2() {
        this(null, null);
    }

    public BasicStringAttribute2(String name) {
        this(name, null);
    }

    public BasicStringAttribute2(String name, String value) {
        setName(name);
        setString(value);
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public void setString(String value) {
        this.value = value;
    }

    @Override
    public BasicStringAttribute2 copy() {
        return new BasicStringAttribute2(getName(), getString());
    }
}
