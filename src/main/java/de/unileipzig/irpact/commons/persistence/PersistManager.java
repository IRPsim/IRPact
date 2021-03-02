package de.unileipzig.irpact.commons.persistence;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public interface PersistManager {

    <T> void persist(T object);

    long newUID();

    Collection<Persistable> getPersistables();

    <T> void prepare(T object);

    default void prepareAll(Collection<?> coll) {
        for(Object object: coll) {
            prepare(object);
        }
    }

    default void prepareAll(Map<?, ?> map) {
        for(Map.Entry<?, ?> entry: map.entrySet()) {
            prepare(entry.getKey());
            prepare(entry.getValue());
        }
    }

    <T> long ensureGetUID(T object) throws NoSuchElementException;

    default <T> long[] ensureGetAllUIDs(Collection<? extends T> coll) throws NoSuchElementException {
        long[] ids = new long[coll.size()];
        int index = 0;
        for(T object: coll) {
            long id = ensureGetUID(object);
            ids[index++] = id;
        }
        return ids;
    }

    default <K, V> Map<Long, Long> ensureGetAllUIDs(Map<? extends K, ? extends V> map) throws NoSuchElementException {
        Map<Long, Long> idMap = new LinkedHashMap<>();
        for(Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
            long keyId = ensureGetUID(entry.getKey());
            long valueId = ensureGetUID(entry.getValue());
            if(idMap.containsKey(keyId)) {
                throw new IllegalArgumentException("id '" + keyId + "' already exists");
            }
            idMap.put(keyId, valueId);
        }
        return idMap;
    }
}
