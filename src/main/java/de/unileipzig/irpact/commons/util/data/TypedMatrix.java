package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public interface TypedMatrix<M, N, V> {

    boolean has(M m, N n);

    V get(M m, N n);

    V getOrDefault(M m, N n, V defaultValue);

    V set(M m, N n, V value);

    String print();
}
