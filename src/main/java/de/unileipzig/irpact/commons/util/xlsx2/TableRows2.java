package de.unileipzig.irpact.commons.util.xlsx2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public final class TableRows2<T> {

    private final List<List<T>> rows;

    public TableRows2(List<List<T>> rows) {
        this.rows = rows;
    }

    public List<List<T>> getRows() {
        return rows;
    }

    public List<List<T>> getColumns() {
        int columnCount = getNumberOfColumns();
        List<List<T>> columns = new ArrayList<>(columnCount);
        for(int i = 0; i < columnCount; i++) {
            List<T> column = getColumn(i);
            columns.add(column);
        }
        return columns;
    }

    public boolean hasEmptyColumn() {
        int columnCount = getNumberOfColumns();
        for(int i = 0; i < columnCount; i++) {
            boolean empty = true;
            for(List<T> row: getRows()) {
                if(row != null && i < row.size() && row.get(i) != null) {
                    empty = false;
                    break;
                }
            }
            if(empty) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEmptyRow() {
        for(List<T> row: rows) {
            if(row == null || row.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEmptyCell() {
        int columnCount = getNumberOfColumns();
        for(List<T> row: rows) {
            if(row == null || row.isEmpty()) {
                return true;
            }
            if(row.contains(null)) {
                return true;
            }
            if(row.size() < columnCount) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfRows() {
        return rows.size();
    }

    public int getNumberOfColumns() {
        int count = 0;
        for(List<T> row: getRows()) {
            if(row != null && row.size() > count) {
                count = row.size();
            }
        }
        return count;
    }

    public List<T> getColumn(int index) {
        List<T> column = new ArrayList<>(getNumberOfRows());
        for(List<T> row: getRows()) {
            if(row == null || row.size() < index) {
                column.add(null);
            } else {
                column.add(row.get(index));
            }
        }
        return column;
    }

    public List<T> getRow(int index) {
        return rows.get(index);
    }

    public T get(int rowIndex, int columnIndex) {
        List<T> row = getRow(rowIndex);
        if(row == null || row.size() <= columnIndex) {
            return null;
        } else {
            return row.get(columnIndex);
        }
    }

    public boolean isWellDefined() {
        return !hasEmptyCell();
    }

    public void fill() {
        fill(null, ArrayList::new);
    }

    public void fill(T nullValue, Supplier<? extends List<T>> rowSupplier) {
        int columnCount = getNumberOfColumns();
        for(int i = 0; i < getNumberOfRows(); i++) {
            List<T> row = getRow(i);
            if(row == null) {
                row = rowSupplier.get();
                getRows().set(i, row);
            }
            while(row.size() < columnCount) {
                row.add(nullValue);
            }
        }
    }

    public void update(Function<? super T, ? extends T> updater) {
        if(updater == null) {
            throw new NullPointerException("updater");
        }

        for(List<T> row: getRows()) {
            if(row == null) {
                continue;
            }

            for(int i = 0; i < row.size(); i++) {
                row.set(i, updater.apply(row.get(i)));
            }
        }
    }

    public String print(
            Function<? super T, ? extends String> nonnullPrinter,
            Supplier<? extends String> nullPrinter,
            String emptyText,
            String newLine,
            String delimiter) {
        return print(element -> element == null ? nullPrinter.get() : nonnullPrinter.apply(element), emptyText, newLine, delimiter);
    }

    public String print(
            Function<? super T, ? extends String> elementPrinter,
            String emptyText,
            String newLine,
            String delimiter) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < rows.size(); i++) {
            if(i > 0) {
                sb.append(newLine);
            }

            List<T> row = rows.get(i);
            if(row == null || row.isEmpty()) {
                sb.append(emptyText);
            } else {
                for(int j = 0; j < row.size(); j++) {
                    if(j > 0) {
                        sb.append(delimiter);
                    }

                    sb.append(elementPrinter.apply(row.get(j)));
                }
            }
        }
        return sb.toString();
    }
}
