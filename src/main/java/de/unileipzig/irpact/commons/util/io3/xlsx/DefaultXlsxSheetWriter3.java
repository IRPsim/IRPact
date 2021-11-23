package de.unileipzig.irpact.commons.util.io3.xlsx;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Daniel Abitz
 */
public class DefaultXlsxSheetWriter3<T> extends XlsxSheetWriter3<T> {

    public DefaultXlsxSheetWriter3() {
    }

    @Override
    public XSSFWorkbook newBook() {
        return new XSSFWorkbook();
    }

    @Override
    public Sheet newSheet(Workbook book, String name) {
        return name == null
                ? book.createSheet()
                : book.createSheet(name);
    }
}
