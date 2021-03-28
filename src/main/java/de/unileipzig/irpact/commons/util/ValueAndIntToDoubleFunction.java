package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
@FunctionalInterface
public interface ValueAndIntToDoubleFunction<T> {

    double applyAsDouble(T value, int i);
}
