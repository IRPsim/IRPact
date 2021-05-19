package de.unileipzig.irpact.commons.util.xlsx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.commons.util.IRPactJson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class ComplexXlsxTable {

    private static final Function<? super JsonNode, ? extends String> TEXT_VALUE = node -> {
        if(node.getNodeType() != JsonNodeType.STRING) {
            throw new IllegalArgumentException("no string: " + node.getNodeType());
        }
        return node.textValue();
    };

    protected String[] header;
    protected List<ArrayNode> rows;

    public ComplexXlsxTable() {
        this(new ArrayList<>());
    }

    public ComplexXlsxTable(List<ArrayNode> rows) {
        this.rows = rows;
    }

    //=========================
    //util
    //=========================

    public static Predicate<? super JsonNode> eqString(String input) {
        return node -> {
            if (node.getNodeType() != JsonNodeType.STRING) {
                throw new IllegalArgumentException("no string");
            }
            return Objects.equals(node.textValue(), input);
        };
    }

    public static Predicate<? super JsonNode> eqDouble(double input) {
        return node -> {
            if (node.getNodeType() != JsonNodeType.NUMBER) {
                throw new IllegalArgumentException("no number");
            }
            return node.doubleValue() == input;
        };
    }

    private static JsonNode get(ArrayNode arr, int index) {
        JsonNode node = arr.get(index);
        if(node == null) {
            throw new IllegalArgumentException("column index '" + index + "' not found");
        }
        return node;
    }

    private static ArrayNode extractRowData(Row row, int headerLength, String sheetName, JsonNodeCreator creator) {
        ArrayNode out = creator.arrayNode(headerLength);
        Iterator<Cell> cellIter = row.cellIterator();
        int index = 0;
        while(cellIter.hasNext() && index < headerLength) {
            Cell cell = cellIter.next();
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue = cell.getNumericCellValue();
                    out.add(numValue);
                    break;

                case STRING:
                    String strValue = cell.getStringCellValue();
                    out.add(strValue);
                    break;

                default:
                    if(cell.getCellType() == CellType.BLANK) {
                        if(index == 0) {
                            if(XlsxUtil.isBlankRow(cellIter, headerLength)) {
                                return null;
                            } else {
                                throw new IllegalArgumentException("unsupported cell type: "
                                        + cell.getCellType()
                                        + " (" + sheetName + ", " + index + ")"
                                );
                            }
                        } else {
                            String blankValue = cell.getStringCellValue();
                            out.add(blankValue);
                        }
                    } else {
                        throw new IllegalArgumentException("unsupported cell type: "
                                + cell.getCellType()
                                + " (" + sheetName + ", " + index + ")"
                        );
                    }
                    break;
            }
            index++;
        }
        return out;
    }

    //=========================
    //stuff
    //=========================

    public void parse(XSSFSheet sheet) {
        parse(sheet, IRPactJson.JSON.getNodeFactory());
    }

    public void parse(XSSFSheet sheet, JsonNodeCreator creator) {
        Iterator<Row> rowIter = sheet.rowIterator();
        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("empty sheet");
        }
        rowIter.next(); //skip info row

        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("missing header");
        }
        Row headerRow = rowIter.next();
        String[] header = XlsxUtil.extractHeader(headerRow);
        setHeader(header);

        while(rowIter.hasNext()) {
            Row dataRow = rowIter.next();
            ArrayNode rowNode = extractRowData(dataRow, header.length, sheet.getSheetName(), creator);
            if(rowNode == null) {
                //blank row -> end of data
                break;
            }
            addRow(rowNode);
        }
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[] getHeader() {
        return header;
    }

    public void addRow(ArrayNode row) {
        rows.add(row);
    }

    public int getNumberOfRows() {
        return rows.size();
    }

    public int findColumn(String headerText) {
        for(int i = 0; i < header.length; i++) {
            if(Objects.equals(header[i], headerText)) {
                return i;
            }
        }
        return -1;
    }

    public int findValidColumn(String headerText) {
        int columnIndex = findColumn(headerText);
        if(columnIndex == -1) {
            throw new IllegalArgumentException("header '" + headerText + "' not found");
        }
        return columnIndex;
    }

    public DataType getDataType(int rowNumber, int columnNumber) {
        ArrayNode row = rows.get(rowNumber);
        JsonNode cell = get(row, columnNumber);
        switch (cell.getNodeType()) {
            case STRING:
                return DataType.STRING;

            case NUMBER:
                return DataType.DOUBLE;

            default:
                return DataType.UNKNOWN;
        }
    }

    protected void validateType(DataType type, int rowNumber, int columnNumber) {
        DataType cellType = getDataType(rowNumber, columnNumber);
        if(cellType != type) {
            throw new IllegalArgumentException("type mismatch: " + type + " != " + cellType);
        }
    }

    public JsonNode getCell(ArrayNode row, String headerText) {
        int columnIndex = findValidColumn(headerText);
        return row.get(columnIndex);
    }

    public ArrayNode findRow(int startIndex, int columnIndex, Predicate<? super JsonNode> columnValueTester) {
        for(int i = startIndex; i < getNumberOfRows(); i++) {
            ArrayNode row = rows.get(i);
            JsonNode cell = get(row, columnIndex);
            if(columnValueTester.test(cell)) {
                return row;
            }
        }
        return null;
    }

    public Stream<ArrayNode> streamRows(int columnIndex, Predicate<? super JsonNode> columnValueTester) {
        return rows.stream()
                .filter(arr -> {
                    JsonNode cell = get(arr, columnIndex);
                    return columnValueTester.test(cell);
                });
    }

    public Map<String, List<ArrayNode>> mapReduce(int columnIndex) {
        return mapReduce(columnIndex, TEXT_VALUE);
    }

    public Map<String, List<ArrayNode>> mapReduce(int columnIndex, Function<? super JsonNode, ? extends String> func) {
        return rows.stream()
                .collect(Collectors.groupingBy(arr -> {
                    JsonNode cell = get(arr, columnIndex);
                    return func.apply(cell);
                }));
    }
}
