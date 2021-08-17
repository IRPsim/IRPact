package de.unileipzig.irpact.commons.util.table;

/**
 * @author Daniel Abitz
 */
public interface ValueConverter<T, R> {

    R convert(Header header, int columnIndex, T value);
}
