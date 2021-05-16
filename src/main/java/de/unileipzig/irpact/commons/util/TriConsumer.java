package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);
}
