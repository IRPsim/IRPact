package de.unileipzig.irpact.commons.util.io;

/**
 * @author Daniel Abitz
 */
public interface ValueConverter<T, R> {

    R convert(Header header, int columnIndex, T value);
}
