package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public class GroupEntityDoubleAttribute<T> extends DoubleAttribute implements GroupEntityValueAttribute<T> {

    protected T group;

    public void setGroup(T group) {
        this.group = group;
    }

    @Override
    public T getGroup() {
        return group;
    }
}
