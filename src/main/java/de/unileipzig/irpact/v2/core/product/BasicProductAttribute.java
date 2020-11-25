package de.unileipzig.irpact.v2.core.product;

import de.unileipzig.irpact.v2.commons.attribute.DoubleAttributeGroupEntityBase;

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
