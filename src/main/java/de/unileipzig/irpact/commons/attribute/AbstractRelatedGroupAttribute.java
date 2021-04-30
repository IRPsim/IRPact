package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRelatedGroupAttribute<T> extends AbstractRelatedAttribute<T> implements RelatedGroupAttribute<T> {

    public AbstractRelatedGroupAttribute() {
        super();
    }

    public AbstractRelatedGroupAttribute(MapSupplier supplier) {
        super(supplier);
    }
}
