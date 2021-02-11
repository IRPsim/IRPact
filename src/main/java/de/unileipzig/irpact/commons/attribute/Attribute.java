package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface Attribute<T> extends AttributeBase {

    T getValue();

    void setValue(T value);
}
