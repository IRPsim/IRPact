package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.util.table.Header;
import de.unileipzig.irpact.commons.util.table.ValueConverter;

/**
 * @author Daniel Abitz
 */
public interface CellValueConverter<T, R> extends ValueConverter<T, R> {

    default boolean isSupported(Header header, int columnIndex, T value) {
        return false;
    }
}
