package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface IndexFunction<T, R> {

    R apply(T t, int index);
}
