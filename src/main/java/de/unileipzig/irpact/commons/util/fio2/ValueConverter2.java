package de.unileipzig.irpact.commons.util.fio2;

/**
 * @author Daniel Abitz
 */
public interface ValueConverter2<T, R> {

    R convert(int columnIndex, T value);
}
