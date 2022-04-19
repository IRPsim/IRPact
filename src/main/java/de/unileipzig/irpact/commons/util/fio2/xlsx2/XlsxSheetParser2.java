package de.unileipzig.irpact.commons.util.fio2.xlsx2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.exception.UnsupportedCellTypeException;
import de.unileipzig.irpact.commons.util.TriConsumer;
import de.unileipzig.irpact.commons.util.fio2.Rows;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class XlsxSheetParser2<T> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(XlsxSheetParser2.class);

    protected boolean throwException = false;

    protected CellValueConverter2<Number, T> numericConverter;
    protected CellValueConverter2<String, T> textConverter;
    protected CellValueConverter2<Void, T> blankConverter;

    @SuppressWarnings("rawtypes")
    protected Supplier rowSupplier;
    @SuppressWarnings("rawtypes")
    protected TriConsumer rowElementHandler;
    @SuppressWarnings("rawtypes")
    protected BiConsumer rowConsumer;

    protected XSSFSheet sheet;

    public XlsxSheetParser2() {
    }

    //=========================
    //access
    //=========================

    public <A> void setRowHandling(
            Supplier<A> rowSupplier,
            TriConsumer<A, Integer, T> rowElementHandler,
            BiConsumer<A, Integer> rowConsumer) {
        this.rowSupplier = rowSupplier;
        this.rowElementHandler = rowElementHandler;
        this.rowConsumer = rowConsumer;
    }

    public void setNumericConverter(CellValueConverter2<Number, T> numericConverter) {
        this.numericConverter = numericConverter;
    }

    public void setTextConverter(CellValueConverter2<String, T> textConverter) {
        this.textConverter = textConverter;
    }

    public void setBlankConverter(CellValueConverter2<Void, T> blankConverter) {
        this.blankConverter = blankConverter;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    //=========================
    //parse
    //=========================

    public Rows<T> parse(Path input) throws ParsingException, IOException, InvalidFormatException {
        return parse(input, 0);
    }

    public Rows<T> parse(InputStream in, int sheetIndex) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(in, sheetIndex, list);
        return new Rows<>(list);
    }

    public Rows<T> parse(InputStream in, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(in, sheetName, list);
        return new Rows<>(list);
    }

    public Rows<T> parse(Path input, int sheetIndex) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(input, sheetIndex, list);
        return new Rows<>(list);
    }

    public Rows<T> parse(Path input, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(input, sheetName, list);
        return new Rows<>(list);
    }

    public Rows<T> parse(XSSFSheet sheet) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(sheet, list);
        return new Rows<>(list);
    }

    public boolean collect(InputStream in, int sheetIndex, Collection<? super List<T>> target) throws ParsingException, IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(in)) {
            return collect(book.getSheetAt(sheetIndex), target);
        }
    }

    public boolean collect(InputStream in, String sheetName, Collection<? super List<T>> target) throws ParsingException, IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(in)) {
            XSSFSheet sheet = book.getSheet(sheetName);
            if(sheet == null) {
                throw new NoSuchElementException("sheet '" + sheetName + "' not found");
            }
            return collect(sheet, target);
        }
    }

    public boolean collect(Path input, int sheetIndex, Collection<? super List<T>> target) throws ParsingException, IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(input.toFile())) {
            return collect(book.getSheetAt(sheetIndex), target);
        }
    }

    public boolean collect(Path input, String sheetName, Collection<? super List<T>> target) throws ParsingException, IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(input.toFile())) {
            XSSFSheet sheet = book.getSheet(sheetName);
            if(sheet == null) {
                throw new NoSuchElementException("sheet '" + sheetName + "' not found");
            }
            return collect(sheet, target);
        }
    }

    public boolean collect(XSSFSheet sheet, Collection<? super List<T>> target) throws ParsingException {
        try {
            setSheet(sheet);

            NavigableMap<Integer, NavigableMap<Integer, T>> rowData = new TreeMap<>();
            setRowHandling(
                    (Supplier<TreeMap<Integer, T>>) TreeMap::new,
                    TreeMap::put,
                    (map, rowIndex) -> rowData.put(rowIndex, map)
            );

            parse();

            if(rowData.isEmpty()) {
                return false;
            }

            int numberOfRows = rowData.lastKey() + 1;
            List<List<T>> tableList = new ArrayList<>(numberOfRows);
            for(int i = 0; i < numberOfRows; i++) {
                tableList.add(null);
            }
            for(Map.Entry<Integer, NavigableMap<Integer, T>> row: rowData.entrySet()) {
                int numberOfColumns = row.getValue().lastKey() + 1;
                List<T> rowList = new ArrayList<>(numberOfColumns);
                for(int i = 0; i < numberOfColumns; i++) {
                    rowList.add(null);
                }
                for(Map.Entry<Integer, T> entry: row.getValue().entrySet()) {
                    rowList.set(entry.getKey(), entry.getValue());
                }
                tableList.set(row.getKey(), rowList);
            }

            boolean changed = false;
            for(List<T> row: tableList) {
                changed |= target.add(row);
            }
            return changed;
        } finally {
            setRowHandling(null, null, null);
            setSheet(null);
        }
    }

    //=========================
    //intern
    //=========================

    protected void resetParse() {
    }

    @SuppressWarnings("unchecked")
    protected boolean handleRow(Row row) throws ParsingException {
        Object rowElement = rowSupplier.get();
        int rowIndex = row.getRowNum();

        Iterator<Cell> cellIter = row.cellIterator();
        while(cellIter.hasNext()) {
            Cell cell = cellIter.next();
            int columnIndex = cell.getColumnIndex();

            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    if(numericConverter == null) {
                        throw unknownCellType(cell);
                    }

                    double numValue;
                    try {
                        numValue = getFormatedNumericCellValue(cell, throwException);
                    } catch (ParseException e) {
                        throw new ParsingException(e);
                    }
                    T numEntry = numericConverter.convert(columnIndex, numValue);
                    rowElementHandler.accept(rowElement, columnIndex, numEntry);
                    break;

                case STRING:
                    if(textConverter == null) {
                        throw unknownCellType(cell);
                    }

                    String strValue = cell.getStringCellValue();
                    T strEntry = textConverter.convert(columnIndex, strValue);
                    rowElementHandler.accept(rowElement, columnIndex, strEntry);
                    break;

                case BLANK:
                    if(isEmptyRow(row)) {
                        return true;
                    }

                    if(blankConverter == null) {
                        throw unknownCellType(cell);
                    }

                    T blankEntry = blankConverter.convert(columnIndex, null);
                    rowElementHandler.accept(rowElement, columnIndex, blankEntry);
                    break;

                default:
                    throw unknownCellType(cell);
            }
        }
        rowConsumer.accept(rowElement, rowIndex);

        return false;
    }

    protected boolean isEmptyRow(Row row) {
        Iterator<Cell> iter = row.cellIterator();
        while(iter.hasNext()) {
            Cell cell = iter.next();
            if(cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    protected void handleEndOfData() {
    }

    protected void parse() throws ParsingException {
        resetParse();

        Iterator<Row> rowIter = sheet.rowIterator();

        boolean eodCalled = false;
        while(rowIter.hasNext()) {
            Row row = rowIter.next();
            if(handleRow(row)) {
                eodCalled = true;
                break;
            }
        }
        if(!eodCalled) {
            handleEndOfData();
        }
    }

    //=========================
    //util
    //=========================

    private static UnsupportedCellTypeException unknownCellType(Cell cell) {
        return new UnsupportedCellTypeException(
                "unsupported cell type '{}' (sheet: {}, row: {}, column: {})",
                cell.getCellType(), cell.getSheet().getSheetName(),
                cell.getRowIndex(), cell.getColumnIndex()
        );
    }

    public static double getFormatedNumericCellValue(Cell cell, boolean throwException) throws ParseException {
        CellStyle cs = cell.getCellStyle();
        String format = cs.getDataFormatString();
        double value = cell.getNumericCellValue();
        if(!"General".equals(format)) {
            DecimalFormat df = new DecimalFormat(format);
            String formatted = df.format(value);
            try {
                return df.parse(formatted).doubleValue();
            } catch (ParseException e) {
                if(throwException) {
                    throw e;
                } else {
                    LOGGER.warn("parsing failed, use default numeric cell value", e);
                    return value;
                }
            }
        } else {
            return value;
        }
    }
}
