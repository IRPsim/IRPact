package de.unileipzig.irpact.commons.util.io3;

/**
 * @author Daniel Abitz
 */
public interface ValueSetter<C, T> {

    void set(C cell, T value);
}
