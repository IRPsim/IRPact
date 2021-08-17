package de.unileipzig.irpact.commons.util.table;

import de.unileipzig.irpact.commons.util.data.MapBasedTypedMatrix;
import de.unileipzig.irpact.commons.util.data.TypedMatrix;

import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public final class TableUtil {

    protected TableUtil() {
    }

    public static <M, N, V, T> TypedMatrix<M, N, V> toMatrix(
            Table<T> table,
            int mColumn,
            Function<? super String, ? extends N> str2n,
            Function<? super T, ? extends M> t2m,
            Function<? super T, ? extends V> t2v) {
        MapBasedTypedMatrix<M, N, V> matrix = new MapBasedTypedMatrix<>();
        toMatrix(table, mColumn, str2n, t2m, t2v, matrix);
        return matrix;
    }

    public static <M, N, V, T> void toMatrix(
            Table<T> table,
            int mColumn,
            Function<? super String, ? extends N> str2n,
            Function<? super T, ? extends M> t2m,
            Function<? super T, ? extends V> t2v,
            TypedMatrix<M, N, V> target) {
        for(int m = 0; m < table.rowCount(); m++) {
            T mEntry = table.getEntry(mColumn, m);
            M mValue = t2m.apply(mEntry);
            for(int n = 0; n < table.columnCount(); n++) {
                if(n == mColumn) continue;
                String columnName = table.columnName(n);
                N nValue = str2n.apply(columnName);
                T vEntry = table.getEntry(n, m);
                V vValue = t2v.apply(vEntry);
                target.set(mValue, nValue, vValue);
            }
        }
    }
}
