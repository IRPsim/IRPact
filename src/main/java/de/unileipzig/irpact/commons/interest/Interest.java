package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
public interface Interest<T> extends ChecksumComparable {

    boolean isInterested(T item);

    @Todo("entfernen")
    boolean isAware(T item);

    void update(T item, double influence);

    void makeInterested(T item);

    void forget(T item);

    double getValue(T item);
}
