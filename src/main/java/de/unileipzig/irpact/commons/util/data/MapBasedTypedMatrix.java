package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class MapBasedTypedMatrix<M, N, V> implements TypedMatrix<M, N, V> {

    protected Supplier<? extends Map<M, Map<N, V>>> matrixSupplier;
    protected Supplier<? extends Map<N, V>> columnSupplier;
    protected Map<M, Map<N, V>> matrix;

    public MapBasedTypedMatrix() {
        this(LinkedHashMap::new, LinkedHashMap::new);
    }

    public MapBasedTypedMatrix(
            Supplier<? extends Map<M, Map<N, V>>> matrixSupplier,
            Supplier<? extends Map<N, V>> columnSupplier) {
        this.matrixSupplier = matrixSupplier;
        this.columnSupplier = columnSupplier;
        matrix = matrixSupplier.get();
    }

    @Override
    public boolean has(M m, N n) {
        Map<N, V> row = matrix.get(m);
        return row != null && row.containsKey(n);
    }

    @Override
    public V get(M m, N n) {
        return getOrDefault(m, n, null);
    }

    @Override
    public V getOrDefault(M m, N n, V defaultValue) {
        Map<N, V> row = matrix.get(m);
        if(row == null) return defaultValue;
        return row.getOrDefault(n, defaultValue);
    }

    @Override
    public V set(M m, N n, V value) {
        Map<N, V> row = matrix.computeIfAbsent(m, _m -> columnSupplier.get());
        return row.put(n, value);
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Map.Entry<M, Map<N, V>> entry: matrix.entrySet()) {
            if(!first) {
                sb.append(StringUtil.lineSeparator());
            }
            first = false;
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return matrix.toString();
    }
}
