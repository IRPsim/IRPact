package de.unileipzig.irpact.commons.persistence;

/**
 * @author Daniel Abitz
 */
public interface Persister<T> {

    Class<T> getType();

    Persistable persist(T object, PersistManager manager);

//    Diese Methode simuliert einen Speichervorgang.
//    Dient primaer dazu Rnd (>Random) neu zu initialisieren.
//    default void simulatePersist(T object, PersistManager manager) {
//    }
}
