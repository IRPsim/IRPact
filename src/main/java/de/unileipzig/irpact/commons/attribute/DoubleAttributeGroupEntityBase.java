package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public class DoubleAttributeGroupEntityBase<T> extends DoubleAttributeBase implements DoubleAttributeGroupEntity<T> {

    protected T group;

    public void setGroup(T group) {
        this.group = group;
    }

    @Override
    public T getGroup() {
        return group;
    }
}
