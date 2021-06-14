package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.util.io.Header;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class XlsxSheetWriter<T> {

    protected CellValueConverter<T, Number> numericConverter;
    protected CellValueConverter<T, String> textConverter;

    public XlsxSheetWriter() {
    }

    //=========================
    //access
    //=========================

    public void setNumericConverter(CellValueConverter<T, Number> numericConverter) {
        this.numericConverter = numericConverter;
    }

    public void setTextConverter(CellValueConverter<T, String> textConverter) {
        this.textConverter = textConverter;
    }

    //=========================
    //writing
    //=========================

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            Iterable<? extends String> infos,
            Header header,
            Iterable<? extends List<T>> rows) throws IOException {
        return write(target, sheetName, infos.iterator(), header, rows.iterator());
    }

    public XSSFWorkbook write(
            Path target,
            String sheetName,
            Iterator<? extends String> infos,
            Header header,
            Iterator<? extends List<T>> rows) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook();
        write(book, sheetName, infos, header, rows);
        try(OutputStream out = Files.newOutputStream(target)) {
            book.write(out);
        }
        return book;
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            Iterable<? extends String> infos,
            Header header,
            Iterable<? extends List<T>> rows) {
        return write(book, sheetName, infos.iterator(), header, rows.iterator());
    }

    public XSSFSheet write(
            XSSFWorkbook book,
            String sheetName,
            Iterator<? extends String> infos,
            Header header,
            Iterator<? extends List<T>> rows) {
        XSSFSheet sheet = sheetName == null ? book.createSheet() : book.createSheet(sheetName);
        write(sheet, infos, header, rows);
        return sheet;
    }

    public void write(
            XSSFSheet sheet,
            Iterable<? extends String> infos,
            Header header,
            Iterable<? extends List<T>> rows) {
        write(sheet, infos.iterator(), header, rows.iterator());
    }

    public void write(
            XSSFSheet sheet,
            Iterator<? extends String> infos,
            Header header,
            Iterator<? extends List<T>> rows) {

        int rowIndex = 0;
        while(infos.hasNext()) {
            String info = infos.next();
            XSSFRow commentRow = sheet.createRow(rowIndex++);
            if(info != null) {
                XSSFCell cell = commentRow.createCell(0);
                cell.setCellValue(info);
            }
        }

        XSSFRow headerRow = sheet.createRow(rowIndex++);
        for(int i = 0; i < header.length(); i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(header.getLabel(i));
        }

        while(rows.hasNext()) {
            List<T> rowData = rows.next();
            XSSFRow row = sheet.createRow(rowIndex++);
            for(int i = 0; i < rowData.size(); i++) {
                XSSFCell cell = row.createCell(i);
                T value = rowData.get(i);
                if(numericConverter.isSupported(header, i, value)) {
                    Number n = numericConverter.convert(header, i, value);
                    cell.setCellValue(n.doubleValue());
                }
                else if(textConverter.isSupported(header, i, value)) {
                    String str = textConverter.convert(header, i, value);
                    cell.setCellValue(str);
                }
                else {
                    throw new IllegalArgumentException("unsupported: " + value);
                }
            }
        }
    }
}
