package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Awareness<T> extends ChecksumComparable {

    boolean isAware(T item);

    void makeAware(T item);

    void forget(T item);
}
