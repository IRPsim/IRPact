package de.unileipzig.irpact.v2.commons;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface ValueIntToDoubleFunction<T> {

    double applyAsDouble(T value, int i);
}
