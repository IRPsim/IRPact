package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.annotation.ToImpl;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
@ToImpl("statt identifier ein auf enum basierndes type system einbauen")
public interface SimulationCache {

    //=========================
    //get
    //=========================

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

    /*
    <T extends SimulationEntity> void forEach(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Consumer<T> consumer
    );

    <T extends SimulationEntity> void forEach(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Predicate<T> filter,
            Consumer<T> consumer
    );

    <T extends SimulationEntity> Collection<T> select(
            Class<? extends Identifier> idType,
            Class<T> entitiyType
    );

    <T extends SimulationEntity> Collection<T> select(
            Class<? extends Identifier> idType,
            Class<T> entitiyType,
            Predicate<T> filter
    );
    */
}
