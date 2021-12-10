package de.unileipzig.irpact.core.persistence.binaryjson2.persist;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface PersistManager {

    void reset();

    Set<Class<?>> findMissingPersisters(Object obj) throws Throwable;

    void persist(Object obj) throws Throwable;

    default void persistAll(Object... objs) throws Throwable {
        for(Object obj: objs) {
            persist(obj);
        }
    }
}
