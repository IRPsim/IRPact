package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.attribute.AbstractGroupAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractProductGroupAttribute
        extends AbstractGroupAttribute
        implements ProductGroupAttribute {

    @Override
    public abstract AbstractProductGroupAttribute copy();
}
