package de.unileipzig.irpact.commons.attribute.v2;

/**
 * @author Daniel Abitz
 */
public interface RelatedAttribute<T> extends Attribute {

    //=========================
    //Related
    //=========================

    boolean hasAttribute(T related);

    Attribute getAttribute(T related);

    Attribute removeAttribute(T related);

    //=========================
    //Attribute
    //=========================

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.RELATED;
    }

    @Override
    default RelatedAttribute<T> asRelatedAttribute() {
        return this;
    }

    //=========================
    //special
    //=========================
}
