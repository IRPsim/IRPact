package de.unileipzig.irpact.core;

import de.unileipzig.irpact.core.simulation.SimulationEntity;

import java.util.Collection;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public interface Group<T> extends SimulationEntity {

    Collection<T> getEntities();

    boolean addEntity(T entitiy);
}
