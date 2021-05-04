package de.unileipzig.irpact.commons.attribute.v3;

/**
 * @author Daniel Abitz
 */
public interface RelatedAttribute<R> extends Attribute {

    //=========================
    //Related
    //=========================

    boolean hasAttribute(R related);

    AttributeBase getAttribute(R related);

    AttributeBase removeAttribute(R related);

    //=========================
    //Attribute
    //=========================

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.RELATED;
    }

    @SuppressWarnings("unchecked")
    @Override
    default RelatedAttribute<R> asRelatedAttribute() {
        return this;
    }

    //=========================
    //special
    //=========================
}
