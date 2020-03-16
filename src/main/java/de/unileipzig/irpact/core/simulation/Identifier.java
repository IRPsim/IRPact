package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public interface Identifier {

    default boolean is(Identifier identifier) {
        return this == identifier;
    }

    default boolean isType(Identifier identifier) {
        return identifier != null && isType(identifier.getClass());
    }

    default boolean isType(Class<? extends Identifier> type) {
        return type != null && type.isInstance(this);
    }
}
