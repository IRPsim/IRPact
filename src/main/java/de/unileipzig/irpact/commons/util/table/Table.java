package de.unileipzig.irpact.commons.util.table;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface Table<T> {

    //=========================
    //column
    //=========================

    List<String> getHeader();

    String[] getHeaderAsArray();

    int columnCount();

    String columnName(int columnIndex);

    int columnIndex(String columnName);

    boolean hasColumn(String columnName);

    void addColumn(String name);

    void addColumns(String... names);

    boolean removeColumn(String columnName);

    boolean keepColumns(String... keep);

    boolean keepColumns(Collection<? extends String> keep);

    void swapColumns(int columnIndex1, int columnIndex2);

    void swapColumns(String columnName1, String columnName2);

    //=========================
    //row
    //=========================

    List<List<T>> listTable();

    List<T> listRow(int rowIndex);

    int rowCount();

    T getEntry(String columnName, int rowIndex);

    T getEntry(int columnIndex, int rowIndex);

    T setEntry(int columnIndex, int rowIndex, T value);

    void updateEntry(int columnIndex, int rowIndex, Function<? super T, ? extends T> func);

    void addRow();

    @SuppressWarnings("unchecked")
    void addRow(T... columnValues) throws IllegalArgumentException;

    void addRows(int count);

    boolean removeRow(int rowIndex);

    void swapRows(int rowIndex1, int rowIndex2);

    //=========================
    //row
    //=========================

    Stream<T> streamColumn(int columnIndex);

    Stream<T> streamColumn(String columnName);

    Table<T> copyToNewTable(String... columnNames);
}
