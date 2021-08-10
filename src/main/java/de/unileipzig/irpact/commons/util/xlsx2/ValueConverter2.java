package de.unileipzig.irpact.commons.util.xlsx2;

/**
 * @author Daniel Abitz
 */
public interface ValueConverter2<T, R> {

    R convert(int columnIndex, T value);
}
