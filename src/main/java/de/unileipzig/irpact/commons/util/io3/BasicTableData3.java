package de.unileipzig.irpact.commons.util.io3;

import de.unileipzig.irpact.commons.util.data.MapBasedTypedMatrix;
import de.unileipzig.irpact.commons.util.data.TypedMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class BasicTableData3<T> implements TableData3<T> {

    protected final List<List<T>> rows;

    public BasicTableData3() {
        this(new ArrayList<>());
    }

    public BasicTableData3(List<List<T>> rows) {
        this.rows = rows;
    }

    protected T getPlaceholderValue() {
        return null;
    }

    @Override
    public List<List<T>> getRows() {
        return rows;
    }

    @Override
    public List<List<T>> getColumns() {
        int columnCount = getNumberOfColumns();
        List<List<T>> columns = new ArrayList<>(columnCount);
        for(int i = 0; i < columnCount; i++) {
            List<T> column = getColumn(i);
            columns.add(column);
        }
        return columns;
    }

    @Override
    public boolean isValidMatrix() {
        int columnSize = -1;
        for(List<T> row: rows) {
            if(row == null) {
                return false;
            }
            if(columnSize == -1) {
                columnSize = row.size();
            } else {
                if(columnSize != row.size()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
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

    @Override
    public boolean hasEmptyRow() {
        for(List<T> row: rows) {
            if(row == null || row.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
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

    @Override
    public int getNumberOfRows() {
        return rows.size();
    }

    @Override
    public int getNumberOfColumns() {
        int count = 0;
        for(List<T> row: getRows()) {
            if(row != null && row.size() > count) {
                count = row.size();
            }
        }
        return count;
    }

    @Override
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

    protected void fillRows(int until, Supplier<? extends List<T>> rowSupplier) {
        while(rows.size() <= until) {
            rows.add(rowSupplier.get());
        }
    }

    protected void fillRow(List<T> row, int unitl, T filler) {
        while(row.size() <= unitl) {
            row.add(filler);
        }
    }

    public List<T> insertRow(int index) {
        return insertRow(index, ArrayList::new);
    }

    public List<T> insertRow(int index, Supplier<? extends List<T>> rowSupplier) {
        List<T> newRow = rowSupplier.get();
        rows.add(index, newRow);
        return newRow;
    }

    @Override
    public List<T> getRow(int index) {
        return rows.get(index);
    }

    @Override
    public List<T> deleteRow(int index) {
        return rows.remove(index);
    }

    @Override
    public T get(int rowIndex, int columnIndex) {
        List<T> row = getRow(rowIndex);
        if(row == null || row.size() <= columnIndex) {
            return null;
        } else {
            return row.get(columnIndex);
        }
    }

    public void set(int rowIndex, int columnIndex, T value) {
        set(rowIndex, columnIndex, value, ArrayList::new);
    }

    public void set(int rowIndex, int columnIndex, T value, Supplier<? extends List<T>> rowSupplier) {
        fillRows(rowIndex, rowSupplier);
        List<T> row = rows.get(rowIndex);
        fillRow(row, columnIndex, getPlaceholderValue());
        row.set(columnIndex, value);
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

    public List<List<String>> toStringList(Function<? super T, ? extends String> toString) {
        List<List<String>> out = new ArrayList<>();
        for(List<T> row: rows) {
            out.add(row.stream().map(toString).collect(Collectors.toList()));
        }
        return out;
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

    public <M, N, V> TypedMatrix<M, N, V> toMatrix(
            Function<? super T, ? extends M> t2m,
            Function<? super T, ? extends N> t2n,
            Function<? super T, ? extends V> t2v) {
        TypedMatrix<M, N, V> matrix = new MapBasedTypedMatrix<>();
        toMatrix(t2m, t2n, t2v, matrix);
        return matrix;
    }

    public <M, N, V> void toMatrix(
            Function<? super T, ? extends M> t2m,
            Function<? super T, ? extends N> t2n,
            Function<? super T, ? extends V> t2v,
            TypedMatrix<M, N, V> target) {
        if(!isValidMatrix()) {
            throw new IllegalStateException("invalid matrix");
        }

        List<T> nHeader = rows.get(0);
        for(int i = 1; i < rows.size(); i++) {
            List<T> row = rows.get(i);
            T mKey = row.get(0);
            M mValue = t2m.apply(mKey);
            for(int j = 1; j < row.size(); j++) {
                T nKey = nHeader.get(j);
                T value = row.get(j);
                N nValue = t2n.apply(nKey);
                V vValue = t2v.apply(value);
                target.set(mValue, nValue, vValue);
            }
        }
    }
}
