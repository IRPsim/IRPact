package de.unileipzig.irpact.commons.attribute.v3;

/**
 * @author Daniel Abitz
 */
public interface AnnualAttribute extends RelatedAttribute<Number> {

    //=========================
    //Related
    //=========================

    boolean hasAttribute(int year);

    AttributeBase getAttribute(int year);

    AttributeBase removeAttribute(int year);

    @Override
    default boolean hasAttribute(Number related) {
        return hasAttribute(related.intValue());
    }

    @Override
    default AttributeBase getAttribute(Number related) {
        return getAttribute(related.intValue());
    }

    @Override
    default AttributeBase removeAttribute(Number related) {
        return removeAttribute(related.intValue());
    }

    //=========================
    //Attribute
    //=========================

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.ANNUAL;
    }

    @Override
    default AnnualAttribute asRelatedAttribute() {
        return this;
    }

    //=========================
    //special
    //=========================
}
