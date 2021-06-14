package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.util.io.Header;
import de.unileipzig.irpact.commons.util.io.ValueConverter;

/**
 * @author Daniel Abitz
 */
public interface CellValueConverter<T, R> extends ValueConverter<T, R> {

    default boolean isSupported(Header header, int columnIndex, T value) {
        return false;
    }
}
