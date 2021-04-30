package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.DirectDerivable;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableGenericValueGroupAttribute<D, T>
        extends AbstractGenericValueAttribute<T>
        implements DirectDerivable<D>, GroupAttribute {

    public AbstractDerivableGenericValueGroupAttribute() {
        super();
    }

    @Override
    public AbstractDerivableGenericValueGroupAttribute<D, T> copy() {
        throw new UnsupportedOperationException();
    }
}
