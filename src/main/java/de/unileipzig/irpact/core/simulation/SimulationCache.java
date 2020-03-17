package de.unileipzig.irpact.core.simulation;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public interface SimulationCache {

    //=========================
    //get
    //=========================

    Collection<? extends SimulationEntity> getEntities();

    Collection<? extends SimulationEntity> getPartitionedEntities(EntityType type);

    <T extends SimulationEntity> T getEntity(String entitiyName);

    //=========================
    //find
    //=========================

    default <T extends SimulationEntity> T findEntity(String entitiyName) throws NoSuchElementException {
        T entity = getEntity(entitiyName);
        if(entity == null) {
            throw new NoSuchElementException(entitiyName);
        }
        return entity;
    }

    //=========================
    //util
    //=========================

    boolean register(String name, SimulationEntity entity);

    boolean unregister(String name);
}
