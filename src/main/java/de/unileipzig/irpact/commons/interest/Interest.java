package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface Interest<T> extends IsEquals {

    boolean isInterested(T item);

    boolean isAware(T item);

    void update(T item, double influence);

    void makeInterested(T item);

    void forget(T item);

    default double getValue(T item) {
        throw new UnsupportedOperationException();
    }
}
