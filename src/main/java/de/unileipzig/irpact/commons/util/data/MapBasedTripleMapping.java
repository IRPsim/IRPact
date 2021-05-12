package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Simple triple map based on two maps.
 *
 * @author Daniel Abitz
 */
public class MapBasedTripleMapping<A, B, C> implements TripleMapping<A, B, C> {

    protected MapSupplier mapSupplier;
    protected Map<A, Map<B, C>> mapping;

    public MapBasedTripleMapping() {
        this(MapSupplier.LINKED);
    }

    public MapBasedTripleMapping(MapSupplier mapSupplier) {
        this.mapSupplier = mapSupplier;
        mapping = mapSupplier.newMap();
    }

    @Override
    public C put(A a, B b, C c) {
        Map<B, C> map1 = mapping.computeIfAbsent(a, _a -> mapSupplier.newMap());
        return map1.put(b, c);
    }

    @Override
    public C get(A a, B b) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? null
                : map1.get(b);
    }

    @Override
    public C get(A a, B b, C defaultValue) {
        Map<B, C> map1 = mapping.get(a);
        if(map1 == null) {
            return defaultValue;
        }
        C c = map1.get(b);
        return c == null
                ? defaultValue
                : c;
    }

    @Override
    public C remove(A a, B b) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? null
                : map1.remove(b);
    }

    @Override
    public int size(A a) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? 0
                : map1.size();
    }

    @Override
    public int size() {
        return mapping.values()
                .stream()
                .mapToInt(Map::size)
                .sum();
    }

    @Override
    public Stream<C> streamValues(A a) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? Stream.empty()
                : map1.values().stream();
    }

    @Override
    public Iterator<A> iteratorA() {
        return mapping.keySet().iterator();
    }

    @Override
    public Iterator<B> iteratorB(A a) {
        Map<B, ?> map1 = mapping.get(a);
        return map1 == null
                ? Collections.emptyIterator()
                : map1.keySet().iterator();
    }

    @Override
    public Iterator<C> iteratorC(A a) {
        Map<?, C> map1 = mapping.get(a);
        return map1 == null
                ? Collections.emptyIterator()
                : map1.values().iterator();
    }

    @Override
    public Iterator<Map.Entry<B, C>> iteratorBC(A a) {
        Map<B, C> map1 = mapping.get(a);
        return map1 == null
                ? Collections.emptyIterator()
                : map1.entrySet().iterator();
    }
}
