package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.DirectDerivable;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDerivableValueGroupAttribute<D>
        extends AbstractValueAttribute
        implements DirectDerivable<D>, GroupAttribute {

    public AbstractDerivableValueGroupAttribute() {
        super();
    }

    @Override
    public AbstractDerivableValueGroupAttribute<D> copy() {
        throw new UnsupportedOperationException();
    }
}
