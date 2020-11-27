package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.attribute.DoubleAttributeGroupEntityBase;

/**
 * @author Daniel Abitz
 */
public class BasicProductAttribute extends DoubleAttributeGroupEntityBase<ProductGroupAttribute> implements ProductAttribute {

    public BasicProductAttribute() {
    }

    public BasicProductAttribute(String name, ProductGroupAttribute groupAttribute, double value) {
        setName(name);
        setGroup(groupAttribute);
        setDoubleValue(value);
    }
}
