package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.develop.Todo;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
@Todo("TESTEN iterators")
public interface TripleMapping<A, B, C> {

    C put(A a, B b, C c);

    C get(A a, B b);

    C get(A a, B b, C defaultValue);

    C remove(A a, B b);

    int size(A a);

    int size();

    Stream<C> streamValues(A a);

    Iterator<A> iteratorA();

    @SuppressWarnings("NullableProblems")
    default Iterable<A> iterableA() {
        return this::iteratorA;
    }

    Iterator<B> iteratorB(A a);

    default Iterable<B> iterableB(A a) {
        return () -> iteratorB(a);
    }

    Iterator<C> iteratorC(A a);

    default Iterable<C> iterableC(A a) {
        return () -> iteratorC(a);
    }

    Iterator<Map.Entry<B, C>> iteratorBC(A a);

    default Iterable<Map.Entry<B, C>> iterableBC(A a) {
        return () -> iteratorBC(a);
    }
}
