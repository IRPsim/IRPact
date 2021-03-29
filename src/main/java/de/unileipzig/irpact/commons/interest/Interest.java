package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Interest<T> extends ChecksumComparable {

    boolean isInterested(T item);

    void update(T item, double influence);

    void makeInterested(T item);

    void forget(T item);

    double getValue(T item);
}
