package de.unileipzig.irpact.v2.commons.awareness;

/**
 * @author Daniel Abitz
 */
public interface Awareness<T> {

    boolean isAwareOf(T item);

    void update(T item, double influence);

    void forget(T item);
}
