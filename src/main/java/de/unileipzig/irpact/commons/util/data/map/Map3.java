package de.unileipzig.irpact.commons.util.data.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class Map3<A, B, C> {

    protected Function<? super A, ? extends Map<B, C>> map0Creator;
    protected Map<A, Map<B, C>> map;

    public Map3(
            Function<? super A, ? extends Map<B, C>> map0Creator,
            Map<A, Map<B, C>> map) {
        this.map0Creator = map0Creator;
        this.map = map;
    }

    public static <A, B, C> Map3<A, B, C> newHashMap() {
        return new Map3<>(
                a -> new HashMap<>(),
                new HashMap<>()
        );
    }

    public static <A, B, C> Map3<A, B, C> newConcurrentHashMap() {
        return new Map3<>(
                a -> new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()
        );
    }

    public Map<A, Map<B, C>> getMap() {
        return map;
    }

    protected Map<B, C> getMap0(A a) {
        return map.computeIfAbsent(a, map0Creator);
    }

    public C put(A a, B b, C c) {
        return getMap0(a).put(b, c);
    }

    public C get(A a, B b) {
        Map<B, C> map0 = map.get(a);
        if(map0 == null) return null;
        return map0.get(b);
    }

    public C getOrDefault(A a, B b, C defaultValue) {
        Map<B, C> map0 = map.get(a);
        if(map0 == null) return defaultValue;
        return map0.getOrDefault(b, defaultValue);
    }

    public C update(A a, B b, C defaultValue, UnaryOperator<C> op) {
        C current = getOrDefault(a, b, defaultValue);
        C newValue = op.apply(current);
        return put(a, b, newValue);
    }
}
