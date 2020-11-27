package de.unileipzig.irpact.commons.awareness;

/**
 * @author Daniel Abitz
 */
public interface Awareness<T> {

    Awareness<T> emptyCopy();

    Awareness<T> fullCopy();

    boolean isInterested(T item);

    boolean isAwareOf(T item);

    void update(T item, double influence);

    void makeAware(T item);

    void forget(T item);
}
