package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface AnnualAttribute extends RelatedAttribute<Number> {

    //=========================
    //Related
    //=========================

    boolean hasAttribute(int year);

    Attribute getAttribute(int year);

    Attribute removeAttribute(int year);

    @Override
    default boolean hasAttribute(Number related) {
        return hasAttribute(related.intValue());
    }

    @Override
    default Attribute getAttribute(Number related) {
        return getAttribute(related.intValue());
    }

    @Override
    default Attribute removeAttribute(Number related) {
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

    @Override
    default AnnualAttribute asAnnualAttribute() {
        return this;
    }
}
