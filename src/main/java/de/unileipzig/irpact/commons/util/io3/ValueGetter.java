package de.unileipzig.irpact.commons.util.io3;

/**
 * @author Daniel Abitz
 */
public interface ValueGetter<C, T> {

    T get(C cell);
}
