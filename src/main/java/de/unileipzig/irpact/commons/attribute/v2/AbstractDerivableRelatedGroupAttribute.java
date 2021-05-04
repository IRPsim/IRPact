package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class AbstractDerivableRelatedGroupAttribute<D, T>
        extends AbstractAttribute
        implements DependentDerivableGroupAttribute<D, T>, RelatedAttribute<T> {

    protected MapSupplier supplier;
    protected Map<T, DirectDerivableGroupAttribute<D>> mapping;

    public AbstractDerivableRelatedGroupAttribute() {
        this(MapSupplier.getDefault());
    }

    public AbstractDerivableRelatedGroupAttribute(MapSupplier supplier) {
        this.supplier = supplier;
        mapping = supplier.newMap();
    }

    @Override
    public AbstractDerivableRelatedGroupAttribute<D, T> copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAttribute(T related) {
        return mapping.containsKey(related);
    }

    @Override
    public DirectDerivableGroupAttribute<D> getAttribute(T related) {
        return mapping.get(related);
    }

    public DirectDerivableGroupAttribute<D> secureGetAttribute(T related) {
        DirectDerivableGroupAttribute<D> attribute = mapping.get(related);
        if(attribute == null) {
            throw new NoSuchElementException(Objects.toString(related));
        }
        return attribute;
    }

    public void setAttribute(T related, DirectDerivableGroupAttribute<D> attribute) {
        mapping.put(related, attribute);
    }

    @Override
    public DirectDerivableGroupAttribute<D> removeAttribute(T related) {
        return mapping.remove(related);
    }

    @Override
    public D derive(T input) {
        DirectDerivableGroupAttribute<D> attribute = secureGetAttribute(input);
        return attribute.derive();
    }
}
