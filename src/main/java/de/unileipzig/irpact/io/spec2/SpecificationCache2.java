package de.unileipzig.irpact.io.spec2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class SpecificationCache2 {

    protected final Map<String, Object> CACHE = new HashMap<>();

    public SpecificationCache2() {
    }

    public boolean has(String key) {
        return CACHE.containsKey(key);
    }

    public void securePut(String key, Object value) {
        if(has(key)) {
            throw new IllegalArgumentException("key '" + key + "' already exists");
        } else {
            CACHE.put(key, value);
        }
    }

    public void putIfNotExists(String key, Object value) {
        if(!has(key)) {
            securePut(key, value);
        }
    }

    public Object remove(String key) {
        return CACHE.remove(key);
    }

    public Object get(String key) {
        return CACHE.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAs(String key) {
        return (T) get(key);
    }
}
