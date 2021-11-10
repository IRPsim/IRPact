package de.unileipzig.irpact.commons.util.io3.xlsx;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.util.io3.BasicTableData3;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public XSSFWorkbook newBook() {
        return new XSSFWorkbook();
    }

    //=========================
    //util write
    //=========================

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            BasicTableData3<T> rows) throws IOException {
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
        XSSFWorkbook book = newBook();
        write(book, sheetName, rows);
        write(target, book);
        return book;
    }

    public XSSFWorkbook write(
            Path target,
            Map<String, ? extends BasicTableData3<T>> sheetData) throws IOException {
        XSSFWorkbook book = newBook();
        write(target, book, sheetData);
        return book;
    }

    public void write(
            XSSFWorkbook book,
            Map<String, ? extends BasicTableData3<T>> sheetData) throws IOException {
        for(Map.Entry<String, ? extends BasicTableData3<T>> entry: sheetData.entrySet()) {
            write(book, entry.getKey(), entry.getValue());
        }
    }

    public void write(
            Path target,
            XSSFWorkbook book,
            Map<String, ? extends BasicTableData3<T>> sheetData) throws IOException {
        write(book, sheetData);
        write(target, book);
    }

    public void write(Path target, XSSFWorkbook book) throws IOException {
        try(OutputStream out = Files.newOutputStream(target)) {
            book.write(out);
        }
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            BasicTableData3<T> rows) {
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
            BasicTableData3<T> rows) {
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

    public static CellStyle createDefaultDateStyle(XSSFWorkbook book) {
        CellStyle dateStyle = book.createCellStyle();
        CreationHelper helper = book.getCreationHelper();
        dateStyle.setDataFormat(helper.createDataFormat().getFormat("dd.MM.yyyy, hh:mm:ss"));
        return dateStyle;
    }

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

    public static Predicate<JsonNode> testTime(DateTimeFormatter formatter) {
        return value -> {
            if(value != null && value.isTextual()) {
                try {
                    LocalDateTime.parse(value.asText(), formatter);
                    return true;
                } catch (DateTimeParseException e) {
                    return false;
                }
            } else {
                return false;
            }
        };
    }

    public static Function<JsonNode, LocalDateTime> toTime(DateTimeFormatter formatter) {
        return value -> {
            if(value != null && value.isTextual()) {
                return LocalDateTime.parse(value.asText(), formatter);
            } else {
                throw new IllegalArgumentException("no text node");
            }
        };
    }

    public static Function<JsonNode, CellStyle> toCellStyle(CellStyle style) {
        return value -> {
            if(value != null && value.isTextual()) {
                return style;
            } else {
                throw new IllegalArgumentException("no text node");
            }
        };
    }

    public static CellValueSetter<JsonNode> forJson(
            Predicate<? super JsonNode> timeTester,
            Function<? super JsonNode, ? extends LocalDateTime> toTime) {
        return (cell, value) -> {
            if(value == null) {
                cell.setBlank();
                return;
            }

            if(timeTester.test(value)) {
                LocalDateTime ldt = toTime.apply(value);
                cell.setCellValue(ldt);
                return;
            }

            switch (value.getNodeType()) {
                case BOOLEAN:
                    cell.setCellValue(value.booleanValue());
                    break;

                case NUMBER:
                    double d = value.doubleValue();
                    if(Double.isNaN(d)) {
                        cell.setCellErrorValue(FormulaError.NA.getCode());
                    } else {
                        cell.setCellValue(d);
                    }
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

    public static CellValueSetter<JsonNode> forJson(
            Predicate<? super JsonNode> timeTester,
            Function<? super JsonNode, ? extends LocalDateTime> toTime,
            Function<? super JsonNode, ? extends CellStyle> toCellStyle) {
        return (cell, value) -> {
            if(value == null) {
                cell.setBlank();
                return;
            }

            if(timeTester.test(value)) {
                cell.setCellValue(toTime.apply(value));
                cell.setCellStyle(toCellStyle.apply(value));
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
