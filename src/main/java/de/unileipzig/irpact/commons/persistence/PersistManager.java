package de.unileipzig.irpact.commons.persistence;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public interface PersistManager {

    <T> void persist(T object) throws PersistException;

    long newUID();

    Collection<Persistable> getPersistables();

    <T> void prepare(T object) throws PersistException;

    default void prepareAll(Collection<?> coll) throws PersistException {
        for(Object object: coll) {
            prepare(object);
        }
    }

    default void prepareAll(Map<?, ?> map) throws PersistException {
        for(Map.Entry<?, ?> entry: map.entrySet()) {
            prepare(entry.getKey());
            prepare(entry.getValue());
        }
    }

    <T> long ensureGetUID(T object) throws PersistException;

    default <T> long uncheckedEnsureGetUID(T object) throws UncheckedPersistException {
        try {
            return ensureGetUID(object);
        } catch (PersistException e) {
            throw e.unchecked();
        }
    }

    default <T> long[] ensureGetAllUIDs(Collection<? extends T> coll) throws PersistException {
        long[] ids = new long[coll.size()];
        int index = 0;
        for(T object: coll) {
            long id = ensureGetUID(object);
            ids[index++] = id;
        }
        return ids;
    }

    default <K, V> Map<Long, Long> ensureGetAllUIDs(Map<? extends K, ? extends V> map) throws PersistException {
        Map<Long, Long> idMap = new LinkedHashMap<>();
        for(Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
            long keyId = ensureGetUID(entry.getKey());
            long valueId = ensureGetUID(entry.getValue());
            if(idMap.containsKey(keyId)) {
                throw new PersistException("id '" + keyId + "' already exists");
            }
            idMap.put(keyId, valueId);
        }
        return idMap;
    }
}
