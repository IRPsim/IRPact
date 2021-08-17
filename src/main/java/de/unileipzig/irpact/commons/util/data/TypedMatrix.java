package de.unileipzig.irpact.commons.util.data;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface TypedMatrix<M, N, V> {

    Collection<M> getM();

    Collection<N> getN();

    Collection<N> getN(M m);

    boolean has(M m, N n);

    V get(M m, N n);

    V getOrDefault(M m, N n, V defaultValue);

    V set(M m, N n, V value);

    String print();
}
