package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.DependentDerivable;
import de.unileipzig.irpact.commons.util.MapSupplier;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableRelatedGroupAttribute<D, T>
        extends AbstractRelatedAttribute<T>
        implements DependentDerivable<D, T>, GroupAttribute {

    public AbstractDerivableRelatedGroupAttribute() {
        super();
    }

    public AbstractDerivableRelatedGroupAttribute(MapSupplier supplier) {
        super(supplier);
    }

    @Override
    public AbstractDerivableRelatedGroupAttribute<D, T> copy() {
        throw new UnsupportedOperationException();
    }
}
