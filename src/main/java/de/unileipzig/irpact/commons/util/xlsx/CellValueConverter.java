package de.unileipzig.irpact.commons.util.xlsx;

/**
 * @author Daniel Abitz
 */
public interface CellValueConverter<S, T> {

    T convert(int columnIndex, String[] header, S value);
}
