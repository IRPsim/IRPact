package de.unileipzig.irpact.commons.util.xlsx;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class SimpleXlsxTableWriter<T> {

    private Set<Class<? extends T>> usesNumberConverter = new HashSet<>();
    private Set<Class<? extends T>> usesTextConverter = new HashSet<>();
    private WriterFunction<T, Number> numberConverter;
    private WriterFunction<T, String> textConverter;

    public SimpleXlsxTableWriter() {
    }

    public void registerForNumber(Class<? extends T> c) {
        usesNumberConverter.add(c);
    }

    public void registerForText(Class<? extends T> c) {
        usesTextConverter.add(c);
    }

    public void setNumberConverter(WriterFunction<T, Number> numberConverter) {
        this.numberConverter = numberConverter;
    }

    public void setTextConverter(WriterFunction<T, String> textConverter) {
        this.textConverter = textConverter;
    }

    public XSSFWorkbook write(
            String comment,
            String[] header,
            List<List<T>> rows) {
        XSSFWorkbook book = new XSSFWorkbook();
        write(comment, header, rows, book);
        return book;
    }

    public void write(
            String comment,
            String[] header,
            List<List<T>> rows,
            Path outPath) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook();
        write(comment, header, rows, book);
        try(OutputStream out = Files.newOutputStream(outPath)) {
            book.write(out);
        }
    }

    public XSSFSheet write(
            String comment,
            String[] header,
            List<List<T>> rows,
            XSSFWorkbook target) {
        XSSFSheet sheet = target.createSheet();
        int rowIndex = 0;

        XSSFRow commentRow = sheet.createRow(rowIndex++);
        if(comment != null) {
            XSSFCell cell = commentRow.createCell(0);
            cell.setCellValue(comment);
        }

        XSSFRow headerRow = sheet.createRow(rowIndex++);
        for(int i = 0; i < header.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }

        for(List<T> rowList: rows) {
            XSSFRow row = sheet.createRow(rowIndex++);
            for(int i = 0; i < rowList.size(); i++) {
                T value = rowList.get(i);
                XSSFCell cell = row.createCell(i);
                if(usesNumberConverter.contains(value.getClass())) {
                    Number n = numberConverter.convert(value);
                    cell.setCellValue(n.doubleValue());
                }
                else if(usesTextConverter.contains(value.getClass())) {
                    String t = textConverter.convert(value);
                    cell.setCellValue(t);
                }
                else {
                    throw new IllegalArgumentException("unknown: " + value.getClass());
                }
            }
        }

        return sheet;
    }

    /**
     * @author Daniel Abitz
     */
    public interface WriterFunction<T, R> {

        R convert(T value);
    }
}
