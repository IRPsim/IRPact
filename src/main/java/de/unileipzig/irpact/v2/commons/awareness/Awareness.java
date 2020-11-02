package de.unileipzig.irpact.v2.commons.awareness;

/**
 * @author Daniel Abitz
 */
public interface Awareness<T> {

    Awareness<T> emptyCopy();

    Awareness<T> fullCopy();

    boolean isAwareOf(T item);

    void update(T item, double influence);

    void makeAware(T item);

    void forget(T item);
}
