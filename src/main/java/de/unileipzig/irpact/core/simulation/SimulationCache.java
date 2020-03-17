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

    /*
    <T extends SimulationEntity> void forEach(
            EntityType entitiyType,
            Class<T> entitiyClass,
            Consumer<T> consumer
    );

    <T extends SimulationEntity> void forEach(
            EntityType entitiyType,
            Class<T> entitiyClass,
            Predicate<T> filter,
            Consumer<T> consumer
    );

    <T extends SimulationEntity> Collection<T> select(
            EntityType entitiyType,
            Class<T> entitiyClass
    );

    <T extends SimulationEntity> Collection<T> select(
            EntityType entitiyType,
            Class<T> entitiyClass,
            Predicate<T> filter
    );
    */
}
