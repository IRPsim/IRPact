package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.exception.UnsupportedCellTypeException;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.commons.util.io.Header;
import de.unileipzig.irpact.commons.util.io.SimpleHeader;
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
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class XlsxSheetParser<T> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(XlsxSheetParser.class);

    protected int numberOfInfoRows = 0;
    protected boolean throwException = false;

    protected SimpleHeader header;
    protected CellValueConverter<Number, T> numericConverter;
    protected CellValueConverter<String, T> textConverter;
    protected CellValueConverter<Void, T> emptyConverter;
    protected Supplier<? extends List<T>> rowSupplier;
    protected Consumer<? super List<T>> rowConsumer;

    protected XSSFSheet sheet;

    public XlsxSheetParser() {
    }

    //=========================
    //access
    //=========================

    public void setNumberOfInfoRows(int numberOfInfoRows) {
        this.numberOfInfoRows = numberOfInfoRows;
    }

    public void setRowConsumer(Consumer<? super List<T>> rowConsumer) {
        this.rowConsumer = rowConsumer;
    }

    public void setRowSupplier(Supplier<? extends List<T>> rowSupplier) {
        this.rowSupplier = rowSupplier;
    }

    public void setNumericConverter(CellValueConverter<Number, T> numericConverter) {
        this.numericConverter = numericConverter;
    }

    public void setTextConverter(CellValueConverter<String, T> textConverter) {
        this.textConverter = textConverter;
    }

    public void setEmptyConverter(CellValueConverter<Void, T> emptyConverter) {
        this.emptyConverter = emptyConverter;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public Header getHeader() {
        return header;
    }

    //=========================
    //parse
    //=========================

    public List<List<T>> parse(Path input) throws ParsingException, IOException, InvalidFormatException {
        return parse(input, 0);
    }

    public List<List<T>> parse(Path input, int sheetIndex) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(input, sheetIndex, list);
        return list;
    }

    public List<List<T>> parse(Path input, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(input, sheetName, list);
        return list;
    }

    public List<List<T>> parse(XSSFSheet sheet) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(sheet, list);
        return list;
    }

    public boolean collect(Path input, int sheetIndex, Collection<? super List<T>> target) throws ParsingException, IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(input.toFile())) {
            return collect(book.getSheetAt(sheetIndex), target);
        }
    }

    public boolean collect(Path input, String sheetName, Collection<? super List<T>> target) throws ParsingException, IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(input.toFile())) {
            return collect(book.getSheet(sheetName), target);
        }
    }

    public boolean collect(XSSFSheet sheet, Collection<? super List<T>> target) throws ParsingException {
        try {
            MutableBoolean changed = MutableBoolean.falseValue();
            setRowConsumer(row -> changed.or(target.add(row)));
            setSheet(sheet);
            setRowSupplier(ArrayList::new);
            parse();
            return changed.get();
        } finally {
            setRowSupplier(null);
            setSheet(null);
        }
    }

    //=========================
    //intern
    //=========================

    protected static Row nextRowOrNull(Iterator<Row> rowIter) {
        return rowIter.hasNext() ? rowIter.next() : null;
    }

    protected void resetParse() {
        header = null;
    }

    @SuppressWarnings("unused")
    protected void handleInfoRow(int infoRowIndex, Row infoRow) {
    }

    protected void handleHeaderRow(Row headerRow) throws UnsupportedCellTypeException {
        header = (SimpleHeader) parseHeader(headerRow);
    }

    protected static <T> void set(List<T> list, int index, T value) {
        if(index < list.size()) {
            list.set(index, value);
        } else {
            list.add(value);
        }
    }

    protected boolean handleRow(Row row) throws ParsingException {
        List<T> rowData = rowSupplier.get();

        for(int i = 0; i < header.length(); i++) {
            T empty = emptyConverter.convert(header, i, null);
            rowData.add(empty);
        }

        Iterator<Cell> cellIter = row.cellIterator();
        while(cellIter.hasNext()) {
            Cell cell = cellIter.next();
            int columnIndex = cell.getColumnIndex();
            if(columnIndex >= header.length()) {
                break;
            }

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
                    T numEntry = numericConverter.convert(header, columnIndex, numValue);
                    rowData.set(columnIndex, numEntry);
                    break;

                case STRING:
                    if(textConverter == null) {
                        throw unknownCellType(cell);
                    }

                    String strValue = cell.getStringCellValue();
                    T strEntry = textConverter.convert(header, columnIndex, strValue);
                    rowData.set(columnIndex, strEntry);
                    break;

                case BLANK:
                    if(columnIndex == 0) {
                        if(isBlankRow(cellIter, header.length())) {
                            return true;
                        } else {
                            throw unknownCellType(cell);
                        }
                    } else {
                        if(textConverter == null) {
                            throw unknownCellType(cell);
                        }

                        String blankValue = cell.getStringCellValue();
                        T blankEntry = textConverter.convert(header, columnIndex, blankValue);
                        rowData.set(columnIndex, blankEntry);
                        break;
                    }

                default:
                    throw unknownCellType(cell);
            }
        }
        rowConsumer.accept(rowData);

        return false;
    }

    protected void handleEndOfData() {
    }

    protected void parse() throws ParsingException {
        resetParse();

        Iterator<Row> rowIter = sheet.rowIterator();

        for(int i = 0; i < numberOfInfoRows; i++) {
            Row infoRow = nextRowOrNull(rowIter);
            if(infoRow == null && i == 0) throw new IllegalArgumentException("empty sheet");
            if(infoRow == null) throw new IllegalArgumentException("missing info row: " + i);
            handleInfoRow(i, infoRow);
        }

        Row headerRow = nextRowOrNull(rowIter);
        if(headerRow == null && numberOfInfoRows == 0) throw new IllegalArgumentException("empty sheet");
        if(headerRow == null) throw new IllegalArgumentException("missing header");
        handleHeaderRow(headerRow);

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

    public static Header parseHeader(Row row) throws UnsupportedCellTypeException {
        SimpleHeader header = new SimpleHeader();
        parseHeader(row, header);
        return header;
    }

    public static void parseHeader(Row row, SimpleHeader header) throws UnsupportedCellTypeException {
        Iterator<Cell> cellIter = row.cellIterator();
        boolean noBlankCell = true;
        while(cellIter.hasNext() && noBlankCell) {
            Cell cell = cellIter.next();
            switch (cell.getCellType()) {
                case STRING:
                    String label = cell.getStringCellValue();
                    header.set(cell.getColumnIndex(), label, "");
                    break;

                case BLANK:
                    noBlankCell = false;
                    break;

                default:
                    throw new UnsupportedCellTypeException(
                            "Unsupported header cell type '{}' at row {} and column {}",
                            cell.getCellType(),
                            cell.getRowIndex(),
                            cell.getColumnIndex()
                    );
            }
        }
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
}
