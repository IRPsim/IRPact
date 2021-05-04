package de.unileipzig.irpact.commons.attribute.v2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGenericValueAttribute<T> extends AbstractValueAttribute {

    protected T value;

    @Override
    public T getValue() {
        return value;
    }
}
