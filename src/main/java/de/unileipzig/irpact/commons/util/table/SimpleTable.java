package de.unileipzig.irpact.commons.util.table;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.ExceptionUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class SimpleTable<T> implements Table<T> {

    protected List<String> columns = new ArrayList<>();
    protected String[] columnsArray;
    protected List<List<T>> rows = new ArrayList<>();
    protected T nullValue = null;

    public SimpleTable() {
    }

    //=========================
    //util
    //=========================

    public void validate() {
        int columnCount = columnCount();
        for(int i = 0; i < rowCount(); i++) {
            List<T> row = rows.get(i);
            if(row.size() != columnCount) {
                throw ExceptionUtil.create(IllegalStateException::new, "row {} has wrong number of values: {} != {}", i, row.size(), columnCount);
            }
        }
    }

    protected void validateColumnIndex(int columnIndex) {
        if(columnIndex < 0 || columnIndex >= columnCount()) {
            throw new IndexOutOfBoundsException("column index out of bounds");
        }
    }

    public void setNullValue(T nullValue) {
        this.nullValue = nullValue;
    }

    public T getNullValue() {
        return nullValue;
    }

    public void set(String[] header, List<List<T>> rows) {
        set(CollectionUtil.arrayListOf(header), rows);
    }

    public void set(List<String> header, List<List<T>> rows) {
        this.columns = header;
        this.rows = rows;
    }

    //=========================
    //column
    //=========================

    @Override
    public List<String> getHeader() {
        return columns;
    }

    @Override
    public String[] getHeaderAsArray() {
        if(columnsArray == null || columnsArray.length != columns.size()) {
            columnsArray = columns.toArray(new String[0]);
        }
        return columnsArray;
    }

    @Override
    public String columnName(int columnIndex) {
        return columns.get(columnIndex);
    }

    @Override
    public int columnIndex(String columnName) {
        return columns.indexOf(columnName);
    }

    @Override
    public boolean hasColumn(String columnName) {
        return columns.contains(columnName);
    }

    @Override
    public void addColumn(String name) {
        if(hasColumn(name)) {
            throw new IllegalArgumentException();
        }
        columns.add(name);
        for(List<T> row: rows) {
            row.add(getNullValue());
        }
    }

    @Override
    public void addColumns(String... names) {
        for(String name: names) {
            addColumn(name);
        }
    }

    @Override
    public int columnCount() {
        return columns.size();
    }

    @Override
    public boolean keepColumns(String... keep) {
        return keepColumns(Arrays.asList(keep));
    }

    @Override
    public boolean keepColumns(Collection<? extends String> keep) {
        boolean changed = false;
        Set<String> toRemove = new HashSet<>(columns);
        toRemove.removeAll(keep);
        for(String remove: toRemove) {
            changed |= removeColumn(remove);
        }
        return changed;
    }

    @Override
    public boolean removeColumn(String columnName) {
        int columnIndex = columnIndex(columnName);
        if(columnIndex != -1) {
            columns.remove(columnIndex);
            for(List<T> row: rows) {
                row.remove(columnIndex);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void swapColumns(int columnIndex1, int columnIndex2) {
        swapColumns(columnIndex1, columnIndex2, columnName(columnIndex1), columnName(columnIndex2));
    }

    @Override
    public void swapColumns(String columnName1, String columnName2) {
        swapColumns(columnIndex(columnName1), columnIndex(columnName2), columnName1, columnName2);
    }

    protected void swapColumns(int i, int j, String columni, String columnj) {
        columns.set(i, columnj);
        columns.set(j, columni);
        for(List<T> row: rows) {
            T vi = row.get(i);
            T vj = row.get(j);
            row.set(i, vj);
            row.set(j, vi);
        }
    }

    //=========================
    //row
    //=========================

    @Override
    public List<List<T>> listTable() {
        return rows;
    }

    @Override
    public List<T> listRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    public int rowCount() {
        return rows.size();
    }

    @Override
    public T getEntry(String columnName, int rowIndex) {
        int columnIndex = columnIndex(columnName);
        if(columnIndex == -1) {
            throw new IllegalArgumentException();
        }
        return getEntry(columnIndex, rowIndex);
    }

    @Override
    public T getEntry(int columnIndex, int rowIndex) {
        List<T> row = rows.get(rowIndex);
        return row.get(columnIndex);
    }

    @Override
    public T setEntry(int columnIndex, int rowIndex, T value) {
        List<T> row = rows.get(rowIndex);
        T oldValue = row.get(columnIndex);
        row.set(columnIndex, value);
        return oldValue;
    }

    @Override
    public void updateEntry(int columnIndex, int rowIndex, Function<? super T, ? extends T> func) {
        List<T> row = rows.get(rowIndex);
        T oldValue = row.get(columnIndex);
        T newValue = func.apply(oldValue);
        row.set(columnIndex, newValue);
    }

    @Override
    public void addRow() {
        List<T> newRow = new ArrayList<>(columnCount());
        for(int i = 0; i < columnCount(); i++) {
            newRow.add(getNullValue());
        }
        rows.add(newRow);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addRow(T... columnValues) {
        if(columnValues.length != columnCount()) {
            throw new IllegalArgumentException("value.length != column count");
        }
        List<T> newRow = new ArrayList<>();
        Collections.addAll(newRow, columnValues);
        rows.add(newRow);
    }

    @Override
    public void addRows(int count) {
        for(int i = 0; i < count; i++) {
            addRow();
        }
    }

    @Override
    public void swapRows(int i, int j) {
        List<T> rowi = rows.get(i);
        List<T> rowj = rows.get(j);
        rows.set(i, rowj);
        rows.set(j, rowi);
    }

    @Override
    public boolean removeRow(int rowIndex) {
        if(rowIndex < rowCount()) {
            rows.remove(rowIndex);
            return true;
        } else {
            return false;
        }
    }

    //=========================
    //special
    //=========================

    public void select1(
            String column,
            Consumer<? super T> consumer) {
        int index = columnIndex(column);
        for(List<T> row: rows) {
            T item = row.get(index);
            consumer.accept(item);
        }
    }

    public void select2(
            String column1,
            String column2,
            BiConsumer<? super T, ? super T> consumer) {
        int index1 = columnIndex(column1);
        int index2 = columnIndex(column2);
        for(List<T> row: rows) {
            T item1 = row.get(index1);
            T item2 = row.get(index2);
            consumer.accept(item1, item2);
        }
    }

    @Override
    public Stream<T> streamColumn(int columnIndex) {
        validateColumnIndex(columnIndex);
        return rows.stream()
                .map(row -> row.get(columnIndex));
    }

    @Override
    public Stream<T> streamColumn(String columnName) {
        int index = columnIndex(columnName);
        return streamColumn(index);
    }

    protected SimpleTable<T> createNewInstance() {
        SimpleTable<T> copy = new SimpleTable<>();
        copy.setNullValue(getNullValue());
        return copy;
    }

    @Override
    public SimpleTable<T> copyToNewTable(String... columns) {
        SimpleTable<T> copy = createNewInstance();
        copy.addColumns(columns);
        copy.addRows(rowCount());
        for(String column: columns) {
            int thisColumnIndex = columnIndex(column);
            int copyColumnIndex = copy.columnIndex(column);
            for(int rowIndex = 0; rowIndex < rowCount(); rowIndex++) {
                T value = getEntry(thisColumnIndex, rowIndex);
                copy.setEntry(copyColumnIndex, rowIndex, value);
            }
        }
        return copy;
    }
}
