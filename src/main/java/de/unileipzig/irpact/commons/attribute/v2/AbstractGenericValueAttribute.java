package de.unileipzig.irpact.commons.attribute.v2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractGenericValueAttribute<T> extends AbstractValueAttribute {

    protected T value;

    protected abstract Class<T> getTClass();

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = castTo(getTClass(), value);
    }
}
