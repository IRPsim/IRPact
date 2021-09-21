package de.unileipzig.irpact.commons.util.io3.xlsx;

import de.unileipzig.irpact.commons.util.io3.ValueSetter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Daniel Abitz
 */
public interface CellValueSetter<T> extends ValueSetter<Cell, T> {
}
