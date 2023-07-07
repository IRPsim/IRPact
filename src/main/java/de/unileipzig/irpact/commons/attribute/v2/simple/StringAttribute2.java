package de.unileipzig.irpact.commons.attribute.v2.simple;

/**
 * @author Daniel Abitz
 */
public interface StringAttribute2 extends ValueAttribute2<String> {

    @Override
    StringAttribute2 copy();

    @Override
    default AttributeType2 getType() {
        return AttributeType2.STRING;
    }

    @Override
    default boolean isString() {
        return true;
    }

    @Override
    default StringAttribute2 asString() {
        return this;
    }

    @Override
    default String getValue() {
        return getString();
    }

    @Override
    default void setValue(String value) {
        setString(value);
    }

    String getString();

    void setString(String value);
}
