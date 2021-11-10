package de.unileipzig.irpact.commons.util.io3.xlsx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.MissingNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.exception.UnsupportedCellTypeException;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.TriConsumer;
import de.unileipzig.irpact.commons.util.io3.BasicTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
public class XlsxSheetParser3<T> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(XlsxSheetParser3.class);

    protected boolean throwException = false;

    protected CellValueGetter<T> cellHandler;

    @SuppressWarnings("rawtypes")
    protected Supplier rowSupplier;
    @SuppressWarnings("rawtypes")
    protected TriConsumer rowElementHandler;
    @SuppressWarnings("rawtypes")
    protected BiConsumer rowConsumer;

    protected XSSFSheet sheet;

    public XlsxSheetParser3() {
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

    public void setCellHandler(CellValueGetter<T> cellHandler) {
        this.cellHandler = cellHandler;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    //=========================
    //parse
    //=========================

    public BasicTableData3<T> parse(Path input) throws ParsingException, IOException, InvalidFormatException {
        return parse(input, 0);
    }

    public BasicTableData3<T> parse(InputStream in, int sheetIndex) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(in, sheetIndex, list);
        return new BasicTableData3<>(list);
    }

    public BasicTableData3<T> parse(InputStream in, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(in, sheetName, list);
        return new BasicTableData3<>(list);
    }

    public BasicTableData3<T> parse(Path input, int sheetIndex) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(input, sheetIndex, list);
        return new BasicTableData3<>(list);
    }

    public BasicTableData3<T> parse(Path input, String sheetName) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(input, sheetName, list);
        return new BasicTableData3<>(list);
    }

    public BasicTableData3<T> parse(XSSFSheet sheet) throws ParsingException, IOException, InvalidFormatException {
        List<List<T>> list = new ArrayList<>();
        collect(sheet, list);
        return new BasicTableData3<>(list);
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
    protected boolean handleRow(Row row) {
        Object rowElement = rowSupplier.get();
        int rowIndex = row.getRowNum();

        Iterator<Cell> cellIter = row.cellIterator();
        while(cellIter.hasNext()) {
            Cell cell = cellIter.next();
            int columnIndex = cell.getColumnIndex();
            T value = cellHandler.get(cell);
            rowElementHandler.accept(rowElement, columnIndex, value);
        }
        rowConsumer.accept(rowElement, rowIndex);

        return false;
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

    //=========================
    //util
    //=========================

    public static CellValueGetter<JsonNode> forJson() {
        return forJson(JsonUtil.JSON.getNodeFactory());
    }

    public static CellValueGetter<JsonNode> forJson(JsonNodeCreator creator) {
        return cell -> {
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue;
                    try {
                        numValue = getFormatedNumericCellValue(cell, true);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    return creator.numberNode(numValue);

                case BOOLEAN:
                    boolean boolValue = cell.getBooleanCellValue();
                    return creator.booleanNode(boolValue);

                case STRING:
                    String strValue = cell.getStringCellValue();
                    return creator.textNode(strValue);

                case BLANK:
                    return MissingNode.getInstance();

                default:
                    throw new RuntimeException(unknownCellType(cell));
            }
        };
    }

    public static CellValueGetter<SpatialAttribute> forSpatial() {
        return cell -> {
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue;
                    try {
                        numValue = getFormatedNumericCellValue(cell, true);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    return new BasicSpatialDoubleAttribute(null, numValue);

                case BOOLEAN:
                    boolean boolValue = cell.getBooleanCellValue();
                    return new BasicSpatialDoubleAttribute(null, boolValue);

                case STRING:
                    String strValue = cell.getStringCellValue();
                    return new BasicSpatialStringAttribute(null, strValue);

                case BLANK:
                    throw new IllegalArgumentException("blank cell not supported");

                default:
                    throw new RuntimeException(unknownCellType(cell));
            }
        };
    }
}
