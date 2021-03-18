package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface Awareness<T> extends IsEquals {

    boolean isAware(T item);

    void makeAware(T item);

    void forget(T item);
}
