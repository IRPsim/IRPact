package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.commons.exception.KeyAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores objects based on string keys.
 *
 * @author Daniel Abitz
 */
public final class NamedData {

    protected Map<String, Object> cache;

    public NamedData() {
        this(new HashMap<>());
    }

    public NamedData(Map<String, Object> cache) {
        this.cache = cache;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public Object replace(String key, Object value) {
        return cache.put(key, value);
    }

    public void put(String key, Object value) throws KeyAlreadyExistsException {
        if(cache.containsKey(key)) {
            throw new KeyAlreadyExistsException(key);
        }
        cache.put(key, value);
    }

    public boolean has(String key) {
        return cache.containsKey(key);
    }

    public Object remove(String key) {
        return cache.remove(key);
    }

    public void removeAll() {
        cache.clear();
    }

    public Object get(String key) {
        return cache.get(key);
    }

    @SuppressWarnings("unchecked")
    public <R> R getAs(String key) {
        Object value = get(key);
        return (R) value;
    }

    public <R> R getAs(String key, Class<R> c) {
        Object value = get(key);
        return c.cast(value);
    }

    public Object getNonNull(String key) throws NullPointerException {
        Object value = get(key);
        if(value == null) {
            throw new NullPointerException(key);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public <R> R getNonNullAs(String key) {
        Object value = getNonNull(key);
        return (R) value;
    }

    public <R> R getNonNullAs(String key, Class<R> c) {
        Object value = getNonNull(key);
        return c.cast(value);
    }
}
