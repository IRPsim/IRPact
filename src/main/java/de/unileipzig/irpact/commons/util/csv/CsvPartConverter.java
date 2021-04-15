package de.unileipzig.irpact.commons.util.csv;

/**
 * @author Daniel Abitz
 */
public interface CsvPartConverter<T> {

    T convert(int columnIndex, String[] header, String value);
}
