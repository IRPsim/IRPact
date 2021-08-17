package de.unileipzig.irpact.commons.util.data.grouping;

/**
 * @author Daniel Abitz
 */
public interface Grouping<X> {

    boolean isEmpty();

    void add(X element);
}
