package de.unileipzig.irpact.io.spec2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.Pair;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irptools.util.Util;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class SpecificationHelper2 {

    protected JsonNode root;

    public SpecificationHelper2(JsonNode root) {
        this.root = root;
    }

    public static SpecificationHelper2 of(JsonNode root) {
        return new SpecificationHelper2(root);
    }

    public JsonNode root() {
        return root;
    }

    public ObjectNode rootAsObject() {
        return (ObjectNode) root;
    }

    public ArrayNode rootAsArray() {
        return (ArrayNode) root;
    }

    public int size() {
        return root.size();
    }

    public Iterable<JsonNode> iterateNodes() {
        return Util.iterateElements(root);
    }

    public Iterable<SpecificationHelper2> iterateElements() {
        return () -> new Iterator<SpecificationHelper2>() {

            protected Iterator<JsonNode> iter = root.elements();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public SpecificationHelper2 next() {
                JsonNode node = iter.next();
                return new SpecificationHelper2(node);
            }
        };
    }

    public Iterable<Map.Entry<String, SpecificationHelper2>> iterateFields() {
        return () -> new Iterator<Map.Entry<String, SpecificationHelper2>>() {

            protected Iterator<Map.Entry<String, JsonNode>> iter = root.fields();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Map.Entry<String, SpecificationHelper2> next() {
                Map.Entry<String, JsonNode> entry = iter.next();
                return new Pair<>(entry.getKey(), new SpecificationHelper2(entry.getValue()));
            }
        };
    }

    //=========================
    //setter
    //=========================

    public void set(String key, String value) {
        rootAsObject().put(key, value);
    }

    public void set(String key, int value) {
        rootAsObject().put(key, value);
    }

    public void set(String key, long value) {
        rootAsObject().put(key, value);
    }

    public void set(String key, double value) {
        rootAsObject().put(key, value);
    }

    public void set(double key, double value) {
        set(Double.toString(key), value);
    }

    public void set(String key, JsonNode value) {
        rootAsObject().set(key, value);
    }

    public void setAsInt(String key, boolean value) {
        rootAsObject().put(key, value ? 1 : 0);
    }

    //==========
    //multi obj
    //==========

    public void set(String key1, String key2, String value) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        node1.put(key2, value);
    }

    public void set(String key1, String key2, int value) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        node1.put(key2, value);
    }

    public void set(String key1, String key2, long value) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        node1.put(key2, value);
    }

    public void set(String key1, String key2, double value) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        node1.put(key2, value);
    }

    public void set(String key1, String key2, JsonNode value) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        node1.set(key2, value);
    }

    public SpecificationHelper2 getOrCreateObject(String key1, String key2) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        ObjectNode node2 = Util.getOrCreateObject(node1, key2);
        return new SpecificationHelper2(node2);
    }

    public SpecificationHelper2 getOrCreateArray(String key1, String key2) {
        ObjectNode node1 = Util.getOrCreateObject(rootAsObject(), key1);
        ArrayNode node2 = Util.getOrCreateArray(node1, key2);
        return new SpecificationHelper2(node2);
    }

    //==========
    //arr
    //==========

    public boolean hasArrayEntry(String value) {
        for(JsonNode node: iterateNodes()) {
            if(node.isTextual() && Objects.equals(node.textValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public void add(String text) {
        rootAsArray().add(text);
    }

    public void addIfNotExists(String value) {
        if(!hasArrayEntry(value)) {
            add(value);
        }
    }

    //=========================
    //getter
    //=========================

    public boolean has(String field) {
        return root().has(field);
    }

    public boolean hasNot(String field) {
        return !root().has(field);
    }

    public SpecificationHelper2 addObject() {
        return new SpecificationHelper2(addObjectNode());
    }

    public ObjectNode addObjectNode() {
        return rootAsArray().addObject();
    }

    public ObjectNode putObjectNode(String field) {
        return rootAsObject().putObject(field);
    }

    public SpecificationHelper2 get(int index) {
        return new SpecificationHelper2(root.get(index));
    }

    public JsonNode getNode(String field) {
        return root.get(field);
    }

    public SpecificationHelper2 get(String field) {
        return new SpecificationHelper2(root.get(field));
    }

    public SpecificationHelper2 getOrCreateObject(String field) {
        ObjectNode node = Util.getOrCreateObject(rootAsObject(), field);
        return new SpecificationHelper2(node);
    }

    public SpecificationHelper2 getOrCreateArray(String field) {
        ArrayNode node = Util.getOrCreateArray(rootAsObject(), field);
        return new SpecificationHelper2(node);
    }

    //==========
    //multi obj
    //==========

    public SpecificationHelper2 getObject(String field1, String field2) throws ParsingException {
        JsonNode node1 = getNode(field1);
        if(node1 == null || !node1.isObject()) {
            throw new ParsingException("missing field: " + field1);
        }
        JsonNode node2 = node1.get(field2);
        if(node2 == null || !node2.isObject()) {
            throw new ParsingException("missing field: " + field2);
        }
        return new SpecificationHelper2(node2);
    }

    public String getText(String field1, String field2) throws ParsingException {
        JsonNode node1 = getNode(field1);
        if(node1 == null || !node1.isObject()) {
            throw new ParsingException("missing field: " + field1);
        }
        JsonNode node2 = node1.get(field2);
        if(node2 == null || !node2.isTextual()) {
            throw new ParsingException("missing field: " + field2);
        }
        return node2.textValue();
    }

    public int getInt(String field1, String field2) throws ParsingException {
        JsonNode node1 = getNode(field1);
        if(node1 == null || !node1.isObject()) {
            throw new ParsingException("missing field: " + field1);
        }
        JsonNode node2 = node1.get(field2);
        if(node2 == null || !node2.isNumber()) {
            throw new ParsingException("missing field: " + field2);
        }
        return node2.intValue();
    }

    public long getLong(String field1, String field2) throws ParsingException {
        JsonNode node1 = getNode(field1);
        if(node1 == null || !node1.isObject()) {
            throw new ParsingException("missing field: " + field1);
        }
        JsonNode node2 = node1.get(field2);
        if(node2 == null || !node2.isNumber()) {
            throw new ParsingException("missing field: " + field2);
        }
        return node2.longValue();
    }

    public double getDouble(String field1, String field2) throws ParsingException {
        JsonNode node1 = getNode(field1);
        if(node1 == null || !node1.isObject()) {
            throw new ParsingException("missing field: " + field1);
        }
        JsonNode node2 = node1.get(field2);
        if(node2 == null || !node2.isNumber()) {
            throw new ParsingException("missing field: " + field2);
        }
        return node2.doubleValue();
    }

    public JsonNode getNode(String field1, String field2) throws ParsingException {
        JsonNode node1 = getNode(field1);
        if(node1 == null || !node1.isObject()) {
            throw new ParsingException("missing field: " + field1);
        }
        JsonNode node2 = node1.get(field2);
        if(node2 == null) {
            throw new ParsingException("missing field: " + field2);
        }
        return node2;
    }

    //==========
    //obj
    //==========

    public SpecificationHelper2 getObject(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isObject()) {
            throw new ParsingException("missing field: " + field);
        }
        return new SpecificationHelper2(node);
    }

    public SpecificationHelper2 getArray(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isArray()) {
            throw new ParsingException("missing field: " + field);
        }
        return new SpecificationHelper2(node);
    }

    public String getText(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isTextual()) {
            throw new ParsingException("missing field: " + field);
        }
        return node.textValue();
    }

    public String getTextOr(String field, String other) {
        JsonNode node = getNode(field);
        if(node == null || !node.isTextual()) {
            return other;
        }
        return node.textValue();
    }

    public int getInt(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isNumber()) {
            throw new ParsingException("missing field: " + field);
        }
        return node.intValue();
    }

    public long getLong(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isNumber()) {
            throw new ParsingException("missing field: " + field);
        }
        return node.longValue();
    }

    public long getLongOr(String field, long other) {
        JsonNode node = getNode(field);
        if(node == null || !node.isNumber()) {
            return other;
        }
        return node.longValue();
    }

    public double getDouble(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isNumber()) {
            throw new ParsingException("missing field: " + field);
        }
        return node.doubleValue();
    }

    public boolean getBoolean(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isBoolean()) {
            throw new ParsingException("missing field: " + field);
        }
        return node.booleanValue();
    }

    public boolean getIntAsBoolean(String field) throws ParsingException {
        JsonNode node = getNode(field);
        if(node == null || !node.isNumber()) {
            throw new ParsingException("missing field: " + field);
        }
        int v = node.intValue();
        return v == 1;
    }

    //==========
    //direct
    //==========

    public String getText() throws ParsingException {
        if(root == null || !root.isNumber()) {
            throw new ParsingException("no text");
        }
        return root.textValue();
    }

    public int getInt() throws ParsingException {
        if(root == null || !root.isNumber()) {
            throw new ParsingException("no number");
        }
        return root.intValue();
    }

    public double getDouble() throws ParsingException {
        if(root == null || !root.isNumber()) {
            throw new ParsingException("no number");
        }
        return root.doubleValue();
    }
}
