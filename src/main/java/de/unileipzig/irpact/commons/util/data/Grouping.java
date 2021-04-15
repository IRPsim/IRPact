package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public interface Grouping<X> {

    boolean isEmpty();

    void add(X element);
}
