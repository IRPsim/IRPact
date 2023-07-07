package de.unileipzig.irpact.commons.util.io3;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface TableData3<T> {

    List<List<T>> getRows();

    List<List<T>> getColumns();

    boolean isValidMatrix();

    boolean hasEmptyColumn();

    boolean hasEmptyRow();

    boolean hasEmptyCell();

    int getNumberOfRows();

    int getNumberOfColumns();

    List<T> getColumn(int index);

    List<T> getRow(int index);

    List<T> deleteRow(int index);

    T get(int rowIndex, int columnIndex);
}
