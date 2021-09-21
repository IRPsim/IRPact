package de.unileipzig.irpact.commons.util.io3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import de.unileipzig.irpact.commons.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

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

    public JsonTableData3(JsonNodeCreator creator, List<List<JsonNode>> rows) {
        super(rows);
        this.creator = creator;
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

    public void setDouble(int rowIndex, int columnIndex, double value) {
        set(rowIndex, columnIndex, creator.numberNode(value));
    }

    public void setString(int rowIndex, int columnIndex, String value) {
        set(rowIndex, columnIndex, creator.textNode(value));
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
