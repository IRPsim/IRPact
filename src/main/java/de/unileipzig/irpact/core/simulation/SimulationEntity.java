package de.unileipzig.irpact.core.simulation;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface SimulationEntity {

    SimulationEnvironment getEnvironment();

    String getName();

    default boolean is(EntityType type) {
        return false;
    }

    default boolean isAll(Collection<? extends EntityType> types) {
        for(EntityType type: types) {
            if(!is(type)) {
                return false;
            }
        }
        return true;
    }

    default boolean isOnce(Collection<? extends EntityType> types) {
        for(EntityType type: types) {
            if(is(type)) {
                return true;
            }
        }
        return false;
    }
}
