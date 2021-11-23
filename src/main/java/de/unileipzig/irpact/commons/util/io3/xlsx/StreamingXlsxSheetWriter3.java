package de.unileipzig.irpact.commons.util.io3.xlsx;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public class StreamingXlsxSheetWriter3<T> extends XlsxSheetWriter3<T> {

    protected UnaryOperator<SXSSFWorkbook> workbookInitalizer;
    protected UnaryOperator<SXSSFSheet> sheetInitalizer;

    public StreamingXlsxSheetWriter3() {
        useDefaultInitalizer();
    }

    public void useDefaultInitalizer() {
        setWorkbookInitalizer(book -> {
            book.setCompressTempFiles(false);
            return book;
        });
        setSheetInitalizer(sheet -> {
            sheet.setRandomAccessWindowSize(100);
            return sheet;
        });
    }

    public void setWorkbookInitalizer(UnaryOperator<SXSSFWorkbook> workbookInitalizer) {
        this.workbookInitalizer = workbookInitalizer;
    }

    public void setSheetInitalizer(UnaryOperator<SXSSFSheet> sheetInitalizer) {
        this.sheetInitalizer = sheetInitalizer;
    }

    @Override
    public SXSSFWorkbook newBook() {
        SXSSFWorkbook book = new SXSSFWorkbook();
        if(workbookInitalizer != null) {
            book = workbookInitalizer.apply(book);
        }
        return book;
    }

    @Override
    public SXSSFSheet newSheet(Workbook book, String name) {
        SXSSFWorkbook sbook = (SXSSFWorkbook) book;
        SXSSFSheet sheet = name == null
                ? sbook.createSheet()
                : sbook.createSheet(name);
        if(sheetInitalizer != null) {
            sheet = sheetInitalizer.apply(sheet);
        }
        return sheet;
    }
}
