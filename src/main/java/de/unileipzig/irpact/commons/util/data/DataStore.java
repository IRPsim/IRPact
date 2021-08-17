package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface DataStore extends Nameable {

    //=========================
    // general
    //=========================

    void removeAll();

    boolean contains(String key);

    void put(String key, Object obj);

    boolean remove(String key);

    Object get(String key);

    default <R> R getAs(String key, Class<R> c) {
        return c.cast(get(key));
    }

    @SuppressWarnings("unchecked")
    default <R> R getAuto(String key) {
        return (R) get(key);
    }

    boolean hasFlag(String key, Object flag);

    void setFlag(String key, Object flag);

    boolean removeFlag(String key, Object flag);

    //=========================
    // nameable
    //=========================

    default boolean contains(Nameable obj) {
        return obj != null && contains(obj.getName());
    }

    default void put(Nameable obj) {
        if(obj == null) throw new IllegalArgumentException("obj is null");
        put(obj.getName(), obj);
    }

    default boolean remove(Nameable obj) {
        return obj != null && remove(obj.getName());
    }

    default boolean hasFlag(Nameable obj, Object flag) {
        return obj != null && hasFlag(obj.getName(), flag);
    }

    default void setFlag(Nameable obj, Object flag) {
        if(obj == null) throw new IllegalArgumentException("obj is null");
        if(!contains(obj)) throw new IllegalArgumentException("unknown obj");
        setFlag(obj.getName(), flag);
    }

    default boolean removeFlag(Nameable obj, Object flag) {
        return obj != null && removeFlag(obj.getName(), flag);
    }
}
