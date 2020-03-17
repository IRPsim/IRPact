package de.unileipzig.irpact.core.affinity;

/**
 * @author Daniel Abitz
 */
public interface Affinities<T> {

    boolean hasValue(T target);

    double getValue(T target);
}
