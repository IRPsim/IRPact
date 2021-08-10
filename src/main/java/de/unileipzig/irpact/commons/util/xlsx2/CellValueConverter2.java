package de.unileipzig.irpact.commons.util.xlsx2;

/**
 * @author Daniel Abitz
 */
public interface CellValueConverter2<T, R> extends ValueConverter2<T, R> {

    default boolean isSupported(int columnIndex, T value) {
        return false;
    }
}
