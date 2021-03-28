package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);
}
