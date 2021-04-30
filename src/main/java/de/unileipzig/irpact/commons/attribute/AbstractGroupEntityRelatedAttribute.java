package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.MapSupplier;

/**
 * @author Daniel Abitz
 */
public class AbstractGroupEntityRelatedAttribute<R, T> extends AbstractRelatedAttribute<R> implements GroupEntityRelatedAttribute<R, T> {

    protected T group;

    public AbstractGroupEntityRelatedAttribute() {
        super();
    }

    public AbstractGroupEntityRelatedAttribute(MapSupplier supplier) {
        super(supplier);
    }

    public void setGroup(T group) {
        this.group = group;
    }

    @Override
    public T getGroup() {
        return group;
    }
}
