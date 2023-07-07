package de.unileipzig.irpact.core.process2.modular;

import java.util.concurrent.locks.Lock;

/**
 * @author Daniel Abitz
 */
public interface SharedModuleData {

    //=========================
    //general
    //=========================

    void lock();

    void unlock();

    Lock getLock();

    void removeAll();

    //=========================
    //key-value store
    //=========================

    boolean contains(Object key);

    Object put(Object key, Object obj);

    boolean remove(Object key);

    Object get(Object key);

    default <R> R getAs(Object key, Class<R> c) {
        return c.cast(get(key));
    }

    @SuppressWarnings("unchecked")
    default <R> R getAuto(Object key) {
        return (R) get(key);
    }

    //=========================
    //key-key-value store
    //=========================

    boolean contains(Object key1, Object key2);

    void put(Object key1, Object key2, Object obj);

    boolean remove(Object key1, Object key2);

    Object get(Object key1, Object key2);

    default <R> R getAs(Object key1, Object key2, Class<R> c) {
        return c.cast(get(key1, key2));
    }

    @SuppressWarnings("unchecked")
    default <R> R getAuto(Object key1, Object key2) {
        return (R) get(key1, key2);
    }

    default <R> R getOr(Object key1, Object key2, R ifMissing) {
        R value = getAuto(key1, key2);
        return value == null ? ifMissing : value;
    }
}
