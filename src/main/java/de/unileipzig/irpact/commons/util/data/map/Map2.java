package de.unileipzig.irpact.commons.util.data.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class Map2<A, B> {

    protected Map<A, B> map;

    public Map2(Map<A, B> map) {
        this.map = map;
    }

    public static <A, B> Map2<A, B> newHashMap() {
        return new Map2<>(new HashMap<>());
    }

    public static <A, B> Map2<A, B> newConcurrentHashMap() {
        return new Map2<>(new ConcurrentHashMap<>());
    }

    public Map<A, B> getMap() {
        return map;
    }

    public B put(A a, B b) {
        return map.put(a, b);
    }

    public B get(A a) {
        return map.get(a);
    }

    public B getOrDefault(A a, B defaultValue) {
        return map.getOrDefault(a, defaultValue);
    }

    public B update(A a, B defaultValue, UnaryOperator<B> op) {
        B current = getOrDefault(a, defaultValue);
        B newValue = op.apply(current);
        return put(a, newValue);
    }
}
