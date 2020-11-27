package de.unileipzig.irpact.commons;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface ValueIntToDoubleFunction<T> {

    double applyAsDouble(T value, int i);
}
