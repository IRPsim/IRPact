package de.unileipzig.irpact.commons.util.fio2.xlsx2;

import de.unileipzig.irpact.commons.util.fio2.Rows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * @author Daniel Abitz
 */
public class XlsxSheetWriter2<T> {

    protected CellValueConverter2<T, Number> numericConverter;
    protected CellValueConverter2<T, String> textConverter;
    protected CellValueConverter2<T, Void> blankConverter;

    public XlsxSheetWriter2() {
    }

    //=========================
    //access
    //=========================

    public void setNumericConverter(CellValueConverter2<T, Number> numericConverter) {
        this.numericConverter = numericConverter;
    }

    public CellValueConverter2<T, Number> getNumericConverter() {
        return numericConverter;
    }

    public void setTextConverter(CellValueConverter2<T, String> textConverter) {
        this.textConverter = textConverter;
    }

    public CellValueConverter2<T, String> getTextConverter() {
        return textConverter;
    }

    public void setBlankConverter(CellValueConverter2<T, Void> blankConverter) {
        this.blankConverter = blankConverter;
    }

    public CellValueConverter2<T, Void> getBlankConverter() {
        return blankConverter;
    }

    //=========================
    //util write
    //=========================

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            Rows<T> rows) throws IOException {
        return write(target, sheetName, rows.list());
    }

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            Iterable<? extends Iterable<? extends T>> rows) throws IOException {
        return write(target, sheetName, rows.iterator());
    }

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            Iterator<? extends Iterable<? extends T>> rows) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook();
        write(book, sheetName, rows);
        try(OutputStream out = Files.newOutputStream(target)) {
            book.write(out);
        }
        return book;
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            Rows<T> rows) {
        return write(book, sheetName, rows.list());
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            Iterable<? extends Iterable<? extends T>> rows) {
        return write(book, sheetName, rows.iterator());
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            Iterator<? extends Iterable<? extends T>> rows) {
        XSSFSheet sheet = sheetName == null
                ? book.createSheet()
                : book.createSheet(sheetName);
        write(sheet, rows);
        return sheet;
    }

    public void write(
            XSSFSheet sheet,
            Rows<T> rows) {
        write(sheet, rows.list());
    }

    public void write(
            XSSFSheet sheet,
            Iterable<? extends Iterable<? extends T>> rows) {
        write(sheet, rows.iterator());
    }

    //=========================
    //main write
    //=========================

    public void write(
        XSSFSheet sheet,
        Iterator<? extends Iterable<? extends T>> rows) {

        int rowIndex = 0;
        while(rows.hasNext()) {
            Iterable<? extends T> nextRow = rows.next();
            XSSFRow sheetRow = sheet.createRow(rowIndex++);

            if(nextRow == null) {
                continue; //empty row
            }

            int columnIndex = 0;
            for(T cellValue: nextRow) {
                final int currentColumnIndex = columnIndex++;
                XSSFCell sheetCell = sheetRow.createCell(currentColumnIndex);

                if(numericConverter.isSupported(currentColumnIndex, cellValue)) {
                    Number n = numericConverter.convert(currentColumnIndex, cellValue);
                    sheetCell.setCellValue(n.doubleValue());
                }
                else if(textConverter.isSupported(currentColumnIndex, cellValue)) {
                    String text = textConverter.convert(currentColumnIndex, cellValue);
                    sheetCell.setCellValue(text);
                }
                else if(blankConverter.isSupported(currentColumnIndex, cellValue)){
                    sheetCell.setBlank();
                }
                else {
                    throw new IllegalArgumentException("unsupported cell value: " + cellValue);
                }
            }
        }
    }
}
