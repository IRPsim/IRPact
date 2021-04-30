package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface Attribute extends AttributeBase {

    default Attribute copy() {
        throw new UnsupportedOperationException();
    }

    boolean isType(AttributeType type);

    boolean isArtificial();

    default boolean is(Class<?> c) {
        return c.isInstance(this);
    }

    default <R> R as(Class<R> c) {
        return c.cast(this);
    }

    default boolean isValueAttribute() {
        return isType(AttributeType.VALUE);
    }

    default ValueAttribute asValueAttribute() {
        throw new UnsupportedOperationException();
    }

    default boolean isRelatedAttribute() {
        return isType(AttributeType.RELATED);
    }

    default RelatedAttribute<?> asRelatedAttribute() {
        throw new UnsupportedOperationException();
    }

    default boolean isGroupAttribute() {
        return isType(AttributeType.GROUP);
    }

    default GroupAttribute asGroupAttribute() {
        throw new UnsupportedOperationException();
    }
}
