package de.unileipzig.irpact.commons.attribute.v3;

/**
 * @author Daniel Abitz
 */
public interface Attribute extends AttributeBase {

    boolean isType(AttributeType type);

    default boolean isValueAttribute() {
        return isType(AttributeType.VALUE);
    }

    default ValueAttribute<?> asValueAttribute() {
        throw new UnsupportedOperationException();
    }

    default boolean isRelatedAttribute() {
        return isType(AttributeType.RELATED);
    }

    default RelatedAttribute<?> asRelatedAttribute() {
        throw new UnsupportedOperationException();
    }
}
