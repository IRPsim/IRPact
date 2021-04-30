package de.unileipzig.irpact.commons.attribute;

/**
 * @author Daniel Abitz
 */
public interface RelatedGroupAttribute<T> extends GroupAttribute, RelatedAttribute<T> {

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.GROUP || type == AttributeType.RELATED;
    }
}
