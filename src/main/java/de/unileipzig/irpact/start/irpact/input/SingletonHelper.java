package de.unileipzig.irpact.start.irpact.input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class SingletonHelper {

    private final Map<Object, Object> CACHE = new HashMap<>();

    public SingletonHelper() {
    }

    @SuppressWarnings("unchecked")
    public <K, V> V computeIfAbsent(K key, Function<? super K, ? extends V> func) {
        return (V) CACHE.computeIfAbsent(key, (Function<? super Object, ?>) func);
    }
}
