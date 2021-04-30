package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRelatedAttribute<T> extends AbstractAttribute implements RelatedAttribute<T> {

    protected MapSupplier supplier;
    protected Map<T, Attribute> mapping;

    public AbstractRelatedAttribute() {
        this(MapSupplier.getDefault());
    }

    public AbstractRelatedAttribute(MapSupplier supplier) {
        this.supplier = supplier;
        mapping = supplier.newMap();
    }

    @Override
    public boolean hasAttribute(T related) {
        return mapping.containsKey(related);
    }

    @Override
    public Attribute getAttribute(T related) {
        return mapping.get(related);
    }

    public void setAttribute(T related, Attribute attribute) {
        mapping.put(related, attribute);
    }

    @Override
    public Attribute removeAttribute(T related) {
        return mapping.remove(related);
    }
}
