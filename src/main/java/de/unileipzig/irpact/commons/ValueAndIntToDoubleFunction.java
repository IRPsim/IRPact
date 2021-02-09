package de.unileipzig.irpact.commons;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface ValueAndIntToDoubleFunction<T> {

    double applyAsDouble(T value, int i);
}
