package de.unileipzig.irpact.commons.util.io3.xlsx;

import de.unileipzig.irpact.commons.util.io3.ValueGetter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Daniel Abitz
 */
public interface CellValueGetter<T> extends ValueGetter<Cell, T> {
}
