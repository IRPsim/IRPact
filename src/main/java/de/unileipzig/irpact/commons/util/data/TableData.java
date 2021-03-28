package de.unileipzig.irpact.commons.util.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class TableData {

    protected final Map<String, Integer> indexMapping = new HashMap<>();
    protected String[] header;
    protected List<ArrayNode> rows;

    public TableData() {
    }

    private static JsonNode get(ArrayNode arr, int index) {
        JsonNode node = arr.get(index);
        if(node == null) {
            throw new IllegalArgumentException("column index '" + index + "' not found");
        }
        return node;
    }

    public void setHeader(String[] header) {
        this.header = header;
        for(int i = 0; i < header.length; i++) {
            indexMapping.put(header[i], i);
        }
    }

    public void addRow(ArrayNode row) {
        rows.add(row);
    }

    public int getNumberOfRows() {
        return rows.size();
    }

    public int getColumnIndex(String headerName) {
        Integer index = indexMapping.get(headerName);
        return index == null ? -1 : index;
    }

    public int getValidColumnIndex(String headerName) {
        int columnIndex = getColumnIndex(headerName);
        if(columnIndex == -1) {
            throw new IllegalArgumentException("header '" + headerName + "' not found");
        }
        return columnIndex;
    }

    public DataType getDataType(int rowNumber, int columnNumber) {
        ArrayNode row = getRow(rowNumber);
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

    public ArrayNode getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public JsonNode getCell(int rowIndex, int columnIndex) {
        ArrayNode row = getRow(rowIndex);
        return row.get(columnIndex);
    }

    public Map<String, List<ArrayNode>> groupingBy(int columnIndex) {
        return rows.stream()
                .collect(Collectors.groupingBy(arr -> {
                    JsonNode cell = get(arr, columnIndex);
                    if(cell.getNodeType() != JsonNodeType.STRING) {
                        throw new IllegalArgumentException("node has no string");
                    }
                    return cell.textValue();
                }));
    }
}
