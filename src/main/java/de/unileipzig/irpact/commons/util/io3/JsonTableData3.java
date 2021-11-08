package de.unileipzig.irpact.commons.util.io3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import de.unileipzig.irpact.commons.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class JsonTableData3 extends TableData3<JsonNode> {

    protected JsonNodeCreator creator;

    public JsonTableData3() {
        this(JsonUtil.JSON.getNodeFactory());
    }

    public JsonTableData3(JsonNodeCreator creator) {
        this(creator, new ArrayList<>());
    }

    public JsonTableData3(List<List<JsonNode>> rows) {
        this(JsonUtil.JSON.getNodeFactory(), rows);
    }

    public JsonTableData3(JsonNodeCreator creator, List<List<JsonNode>> rows) {
        super(rows);
        this.creator = creator;
    }

    public JsonTableData3 copy() {
        return new JsonTableData3(creator, copy2D(rows));
    }

    protected static List<List<JsonNode>> copy2D(List<List<JsonNode>> input) {
        return input.stream()
                .map(JsonTableData3::copy1D)
                .collect(Collectors.toList());
    }

    protected static List<JsonNode> copy1D(List<JsonNode> input) {
        return input.stream()
                .map(n -> (JsonNode) n.deepCopy())
                .collect(Collectors.toList());
    }

    public JsonNodeCreator getCreator() {
        return creator;
    }

    public int getInt(int rowIndex, int columnIndex) {
        return get(rowIndex, columnIndex).intValue();
    }

    public double getDouble(int rowIndex, int columnIndex) {
        return get(rowIndex, columnIndex).doubleValue();
    }

    public String getString(int rowIndex, int columnIndex) {
        return get(rowIndex, columnIndex).textValue();
    }

    public void setInt(int rowIndex, int columnIndex, int value) {
        set(rowIndex, columnIndex, creator.numberNode(value));
    }

    public void setLong(int rowIndex, int columnIndex, long value) {
        set(rowIndex, columnIndex, creator.numberNode(value));
    }

    public void setDouble(int rowIndex, int columnIndex, double value) {
        set(rowIndex, columnIndex, creator.numberNode(value));
    }

    public void setString(int rowIndex, int columnIndex, String value) {
        set(rowIndex, columnIndex, creator.textNode(value));
    }

    public boolean mapStringToInt(int rowIndex, int columnIndex, ToIntFunction<? super String> func) {
        JsonNode node = get(rowIndex, columnIndex);
        if(node != null && node.isTextual()) {
            try {
                setInt(rowIndex, columnIndex, func.applyAsInt(node.textValue()));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean mapStringToLong(int rowIndex, int columnIndex, ToLongFunction<? super String> func) {
        JsonNode node = get(rowIndex, columnIndex);
        if(node != null && node.isTextual()) {
            try {
                setLong(rowIndex, columnIndex, func.applyAsLong(node.textValue()));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean mapStringToDouble(int rowIndex, int columnIndex, ToDoubleFunction<? super String> func) {
        JsonNode node = get(rowIndex, columnIndex);
        if(node != null && node.isTextual()) {
            try {
                setDouble(rowIndex, columnIndex, func.applyAsDouble(node.textValue()));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void map(int rowIndex, int columnIndex, UnaryOperator<JsonNode> func) {
        JsonNode node = get(rowIndex, columnIndex);
        set(rowIndex, columnIndex, func.apply(node));
    }

    public void mapStringColumnToInt(int columnIndex, int from, ToIntFunction<? super String> func) {
        for(int rowIndex = from; rowIndex < getNumberOfRows(); rowIndex++) {
            mapStringToInt(rowIndex, columnIndex, func);
        }
    }

    public void mapStringColumnToLong(int columnIndex, int from, ToLongFunction<? super String> func) {
        for(int rowIndex = from; rowIndex < getNumberOfRows(); rowIndex++) {
            mapStringToLong(rowIndex, columnIndex, func);
        }
    }

    public void mapStringColumnToDouble(int columnIndex, int from, ToDoubleFunction<? super String> func) {
        for(int rowIndex = from; rowIndex < getNumberOfRows(); rowIndex++) {
            mapStringToDouble(rowIndex, columnIndex, func);
        }
    }

    public void mapColumn(int columnIndex, int from, UnaryOperator<JsonNode> func) {
        for(int rowIndex = from; rowIndex < getNumberOfRows(); rowIndex++) {
            map(rowIndex, columnIndex, func);
        }
    }

    public List<List<String>> toStringList() {
        return toStringList(JsonNode::asText);
    }

    public String print(
            String emptyText,
            String newLine,
            String delimiter) {
        return print(JsonNode::asText, () -> emptyText, emptyText, newLine, delimiter);
    }
}
