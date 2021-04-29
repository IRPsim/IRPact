package de.unileipzig.irpact.commons.util.csv;

/**
 * @author Daniel Abitz
 */
public interface CsvValuePrinter<T> {

    String toString(int columnIndex, String[] header, T value);
}
