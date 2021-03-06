package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.table.SimpleTable;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class XlsxTable<T> extends SimpleTable<T> {

    public XlsxTable() {
    }

    //=========================
    //util
    //=========================

    public void load(XlsxSheetParser<T> parser, Path path) throws IOException, InvalidFormatException, ParsingException {
        load(parser, path, 0);
    }

    public void load(XlsxSheetParser<T> parser, Path path, int sheetIndex) throws IOException, InvalidFormatException, ParsingException {
        List<List<T>> rows = parser.parse(path, sheetIndex);
        set(parser.getHeader().toArray(), rows);
    }

    public void load(XlsxSheetParser<T> parser, Path path, String sheetName) throws IOException, InvalidFormatException, ParsingException {
        List<List<T>> rows = parser.parse(path, sheetName);
        set(parser.getHeader().toArray(), rows);
    }

    public void load(XlsxSheetParser<T> parser, XSSFSheet sheet) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> rows = parser.parse(sheet);
        set(parser.getHeader().toArray(), rows);
    }

    public String printCsv(CsvPrinter<T> printer) {
        return printer.toString(getHeaderAsArray(), listTable());
    }

    @Override
    protected XlsxTable<T> createNewInstance() {
        XlsxTable<T> copy = new XlsxTable<>();
        copy.setNullValue(getNullValue());
        return copy;
    }

    @Override
    public XlsxTable<T> copyToNewTable(String... columns) {
        return (XlsxTable<T>) super.copyToNewTable(columns);
    }
}
