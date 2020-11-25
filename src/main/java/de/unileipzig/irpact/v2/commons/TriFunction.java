package de.unileipzig.irpact.v2.commons;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);
}
