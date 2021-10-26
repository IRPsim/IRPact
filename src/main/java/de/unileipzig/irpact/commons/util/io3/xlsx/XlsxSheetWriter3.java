package de.unileipzig.irpact.commons.util.io3.xlsx;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.util.io3.TableData3;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class XlsxSheetWriter3<T> {

    protected CellValueSetter<T> cellHandler;

    public XlsxSheetWriter3() {
    }

    //=========================
    //access
    //=========================

    public void setCellHandler(CellValueSetter<T> cellHandler) {
        this.cellHandler = cellHandler;
    }

    //=========================
    //util write
    //=========================

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            TableData3<T> rows) throws IOException {
        return write(target, sheetName, rows.getRows());
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
        write(target, book);
        return book;
    }

    public XSSFWorkbook write(
            Path target,
            Map<String, ? extends TableData3<T>> sheetData) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook();
        for(Map.Entry<String, ? extends TableData3<T>> entry: sheetData.entrySet()) {
            write(book, entry.getKey(), entry.getValue());
        }
        write(target, book);
        return book;
    }

    public void write(Path target, XSSFWorkbook book) throws IOException {
        try(OutputStream out = Files.newOutputStream(target)) {
            book.write(out);
        }
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            TableData3<T> rows) {
        return write(book, sheetName, rows.getRows());
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
            TableData3<T> rows) {
        write(sheet, rows.getRows());
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
                XSSFCell sheetCell = sheetRow.createCell(columnIndex++);
                cellHandler.set(sheetCell, cellValue);
            }
        }
    }

    //=========================
    //util
    //=========================

    public static CellValueSetter<JsonNode> forJson() {
        return (cell, value) -> {
            if(value == null) {
                cell.setBlank();
                return;
            }

            switch (value.getNodeType()) {
                case BOOLEAN:
                    cell.setCellValue(value.booleanValue());
                    break;

                case NUMBER:
                    cell.setCellValue(value.doubleValue());
                    break;

                case STRING:
                    cell.setCellValue(value.textValue());
                    break;

                case NULL:
                case MISSING:
                    cell.setBlank();
                    break;
                default:
                    throw new IllegalArgumentException("unsupported node type: " + value.getNodeType());
            }
        };
    }
}
