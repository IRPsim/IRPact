package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Interest<T> extends ChecksumComparable {

    boolean isInterested(T item);

    boolean isInterested(T item, double interest);

    void update(T item, double influence);

    void makeInterested(T item);

    void forget(T item);

    double getValue(T item);
}
