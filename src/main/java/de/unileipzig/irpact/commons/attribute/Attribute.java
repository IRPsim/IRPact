package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface Attribute extends AttributeBase {

    boolean isType(AttributeType type);

    default boolean isValueAttribute() {
        return isType(AttributeType.VALUE);
    }

    default boolean isValueAttributeWithDataType(DataType dataType) {
        return isValueAttribute() && asValueAttribute().isDataType(dataType);
    }

    default boolean isNoValueAttribute() {
        return !isValueAttribute();
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

    default boolean isAnnualAttribute() {
        return isType(AttributeType.ANNUAL);
    }

    default AnnualAttribute asAnnualAttribute() {
        throw new UnsupportedOperationException();
    }
}
