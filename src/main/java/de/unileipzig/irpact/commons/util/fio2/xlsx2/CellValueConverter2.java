package de.unileipzig.irpact.commons.util.fio2.xlsx2;

import de.unileipzig.irpact.commons.util.fio2.ValueConverter2;

/**
 * @author Daniel Abitz
 */
public interface CellValueConverter2<T, R> extends ValueConverter2<T, R> {

    boolean isSupported(int columnIndex, T value);
}
