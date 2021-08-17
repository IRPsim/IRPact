package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class XlsxUtil {

    private XlsxUtil() {
    }

    public static void store(Path path, XSSFWorkbook book) throws IOException {
        try(OutputStream out = Files.newOutputStream(path)) {
            book.write(out);
        }
    }

    public static XSSFWorkbook load(Path path) throws IOException, InvalidFormatException {
        File file = path.toFile();
        return new XSSFWorkbook(file);
    }

    public static Map<String, SimplifiedXlsxTable> extractTablesWithTwoHeaderLines(Path path) throws IOException, InvalidFormatException {
        XSSFWorkbook book = load(path);
        return extractTablesWithTwoHeaderLines(book);
    }

    public static Map<String, SimplifiedXlsxTable> extractTablesWithTwoHeaderLines(XSSFWorkbook book) {
        return extractTablesWithTwoHeaderLines(book, 0);
    }

    public static Map<String, SimplifiedXlsxTable> extractTablesWithTwoHeaderLines(XSSFWorkbook book, int from) {
        Map<String, SimplifiedXlsxTable> tables = new LinkedHashMap<>();
        for(int i = from; i < book.getNumberOfSheets(); i++) {
            String sheetName = book.getSheetName(i);
            if(tables.containsKey(sheetName)) {
                throw new IllegalArgumentException("duplicated sheet name: " + sheetName);
            }

            XSSFSheet sheet = book.getSheetAt(i);
            SimplifiedXlsxTable table = extractTableWithWithTwoHeaderLines(sheet);
            tables.put(sheetName, table);
        }
        return tables;
    }

    public static SimplifiedXlsxTable extractTableWithWithTwoHeaderLines(XSSFSheet sheet) {
        SimplifiedXlsxTable table = new SimplifiedXlsxTable();
        Iterator<Row> rowIter = sheet.rowIterator();

        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("missing header");
        }
        Row headerRow = rowIter.next();
        String[] header = extractHeader(headerRow);
        table.setHeader(header);

        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("missing units");
        }
        rowIter.next(); //ignore row with units

        while(rowIter.hasNext()) {
            Row dataRow = rowIter.next();
            double[] data = extractRowData(dataRow, header.length, sheet.getSheetName());
            if(data == null) {
                //blank row -> end of data
                break;
            }
            table.addRow(data);
        }
        return table;
    }

    public static String[] extractHeader(Row row) {
        List<String> headerList = new ArrayList<>();
        Iterator<Cell> cellIter = row.cellIterator();
        boolean noBlankCell = true;
        while(cellIter.hasNext() && noBlankCell) {
            Cell cell = cellIter.next();
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue = cell.getNumericCellValue();
                    headerList.add(Double.toString(numValue));
                    break;

                case STRING:
                    String strValue = cell.getStringCellValue();
                    headerList.add(strValue);
                    break;

                case BLANK:
                    noBlankCell = false;
                    break;

                default:
                    throw ExceptionUtil.create(IllegalArgumentException::new, "unsupported cell type '{}' at row {} and column {}", cell.getCellType(), cell.getRowIndex(), cell.getColumnIndex());

            }
        }
        return headerList.toArray(new String[0]);
    }

    private static double[] extractRowData(Row row, int headerLength, String sheetName) {
        double[] rowData = new double[headerLength];
        Iterator<Cell> cellIter = row.cellIterator();
        int index = 0;
        while(cellIter.hasNext() && index < headerLength) {
            Cell cell = cellIter.next();
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue = cell.getNumericCellValue();
                    rowData[index] = numValue;
                    break;

                default:
                    if(cell.getCellType() == CellType.BLANK && index == 0) {
                        if(isBlankRow(cellIter, headerLength)) {
                            return null;
                        }
                    }
                    throw new IllegalArgumentException("unsupported cell type: "
                            + cell.getCellType()
                            + " (" + sheetName + ", " + index + ")"
                    );

            }
            index++;
        }
        return rowData;
    }

    public static boolean isBlankRow(Iterator<Cell> cellIter, int headerLength) {
        int index = 1;
        while(cellIter.hasNext() && index < headerLength) {
            Cell cell = cellIter.next();
            if(cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public static KeyValueXlsxTable extractKeyValueTable(XSSFSheet sheet) {
        KeyValueXlsxTable table = new KeyValueXlsxTable();
        Iterator<Row> rowIter = sheet.rowIterator();
        while(rowIter.hasNext()) {
            Row row = rowIter.next();
            Cell keyCell = row.getCell(0);
            Cell valueCell = row.getCell(1);

            String key = keyCell.getStringCellValue();
            if(table.has(key)) {
                throw new IllegalArgumentException("duplicated key detected: " + key);
            }
            table.store(
                    key,
                    valueCell.getNumericCellValue()
            );
        }
        return table;
    }
}
