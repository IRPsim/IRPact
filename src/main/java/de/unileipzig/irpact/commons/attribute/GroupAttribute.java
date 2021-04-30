package de.unileipzig.irpact.commons.attribute;

/**
 * Simple marker interface
 *
 * @author Daniel Abitz
 */
public interface GroupAttribute extends Attribute {

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.GROUP;
    }

    @Override
    default GroupAttribute asGroupAttribute() {
        return this;
    }
}
