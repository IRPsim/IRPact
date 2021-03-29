package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.attribute.DoubleAttributeGroupEntity;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductDoubleAttribute extends DoubleAttributeGroupEntity<ProductGroupAttribute> implements ProductAttribute {

    public ProductDoubleAttribute() {
    }

    public ProductDoubleAttribute(String name, ProductGroupAttribute groupAttribute, double value) {
        setName(name);
        setGroup(groupAttribute);
        setDoubleValue(value);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getGroup().getName(),
                getDoubleValue()
        );
    }
}