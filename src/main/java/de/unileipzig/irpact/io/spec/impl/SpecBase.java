package de.unileipzig.irpact.io.spec.impl;

import de.unileipzig.irpact.io.spec.ToParamFunctionX;
import de.unileipzig.irpact.io.spec.ToSpecFunctionX;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public abstract class SpecBase<S, P> implements ToSpecFunctionX<S>, ToParamFunctionX<P> {

    public SpecBase() {
    }

    @SuppressWarnings("unchecked")
    protected static <T> T find(Map<String, Object> cache, String key) {
        if(cache.containsKey(key)) {
            return (T) cache.get(key);
        } else {
            throw new NoSuchElementException(key);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> T putIfMissing(Map<String, Object> cache, String key, T value) {
        if(cache.containsKey(key)) {
            return (T) cache.get(key);
        } else {
            cache.put(key, value);
            return value;
        }
    }

    protected static <T> void putAll(Map<String, Object> cache, T[] arr, Function<T, String> nameFunc) {
        for(T t: arr) {
            String key = nameFunc.apply(t);
            if(cache.containsKey(key)) {
                throw new IllegalArgumentException("key '" + key + "' already exists");
            } else {
                cache.put(key, t);
            }
        }
    }
}
