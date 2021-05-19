package de.unileipzig.irpact.commons.util.xlsx;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class XlsxSheetParser<T> extends XlsxSheetHandler<T> {

    private Supplier<? extends List<List<T>>> rowsSupplier;
    private List<List<T>> rows;

    public XlsxSheetParser() {
        this(ArrayList::new, ArrayList::new);
    }

    public XlsxSheetParser(
            Supplier<? extends List<List<T>>> rowsSupplier,
            Supplier<? extends List<T>> rowListSupplier) {
        super(rowListSupplier);
        this.rowsSupplier = rowsSupplier;
    }

    public List<List<T>> getRows() {
        check();
        return rows;
    }

    public void parse(Path path) throws IOException, InvalidFormatException {
        parse(path, 0);
    }

    public void parse(Path path, int sheetIndex) throws IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(path.toFile())) {
            parse(book.getSheetAt(sheetIndex));
        }
    }

    public void parse(XSSFSheet sheet) {
        reset();
        this.sheet = sheet;

        parse();
    }

    @Override
    public void reset() {
        super.reset();
        rows = rowsSupplier.get();
    }

    @Override
    protected void handleInfoRow(Row row, int infoRowIndex) {
        //ignore
    }

    @Override
    protected void handleHeader(String[] header) {
        //ignore
    }

    @Override
    protected void handleRow(List<T> row, int rowIndex) {
        rows.add(row);
    }

    @Override
    protected void handleEndOfData() {
        //ignore
    }
}
