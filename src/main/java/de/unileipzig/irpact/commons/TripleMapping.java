package de.unileipzig.irpact.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Simple triple map based on two maps.
 *
 * @author Daniel Abitz
 */
public class TripleMapping<A, B, C> {

    protected Supplier<Map<A, Map<B, C>>> map0Func;
    protected Function<A, Map<B, C>> map1Func;
    protected Map<A, Map<B, C>> mapping;

    public TripleMapping() {
        this(HashMap::new, a -> new HashMap<>());
    }

    public TripleMapping(
            Supplier<Map<A, Map<B, C>>> map0Func,
            Function<A, Map<B, C>> map1Func) {
        this.map0Func = map0Func;
        this.map1Func = map1Func;
        mapping = map0Func.get();
    }

    public C put(A a, B b, C c) {
        Map<B, C> map1 = mapping.computeIfAbsent(a, map1Func);
        return map1.put(b, c);
    }

    public C get(A a, B b) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? null
                : map1.get(b);
    }

    public C remove(A a, B b) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? null
                : map1.remove(b);
    }

    public int size(A a) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? 0
                : map1.size();
    }

    public int size() {
        return mapping.values()
                .stream()
                .mapToInt(Map::size)
                .sum();
    }
}