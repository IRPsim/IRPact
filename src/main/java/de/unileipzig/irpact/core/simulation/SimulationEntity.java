package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public interface SimulationEntity {

    SimulationEnvironment getEnvironment();

    Identifier getIdentifier();

    String getName();

    default boolean is(Identifier identifier) {
        return getIdentifier().is(identifier);
    }

    default boolean isType(Identifier identifier) {
        return getIdentifier().isType(identifier);
    }

    default boolean isType(Class<? extends Identifier> type) {
        return getIdentifier().isType(type);
    }
}
