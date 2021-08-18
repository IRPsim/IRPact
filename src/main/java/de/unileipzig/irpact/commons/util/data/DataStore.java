package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.Nameable;

import java.util.concurrent.locks.Lock;

/**
 * @author Daniel Abitz
 */
public interface DataStore extends Nameable {

    void lock();

    void unlock();

    Lock getLock();

    void removeAll();

    boolean contains(Object key);

    void put(Object key, Object obj);

    boolean remove(Object key);

    Object get(Object key);

    default <R> R getAs(Object key, Class<R> c) {
        return c.cast(get(key));
    }

    @SuppressWarnings("unchecked")
    default <R> R getAuto(Object key) {
        return (R) get(key);
    }

    boolean hasFlag(Object key, Object flag);

    void setFlag(Object key, Object flag);

    boolean removeFlag(Object key, Object flag);
}
