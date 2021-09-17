package de.unileipzig.irpact.commons.resource;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class JsonResource {

    protected JsonNode root;

    public JsonResource(JsonNode root) {
        this.root = root;
    }

    protected JsonNode getWithArray(Object[] arr) {
        JsonNode current = root;
        for(Object obj: arr) {
            current = current.get(obj.toString());
            if(current == null) break;
        }
        return current == null ? MissingNode.getInstance() : current;
    }

    protected JsonNode getWithPointer(JsonPointer ptr) {
        return root.at(ptr);
    }

    protected JsonNode getWithChars(CharSequence csq) {
        JsonNode node = root.get(csq.toString());
        return node == null ? MissingNode.getInstance() : node;
    }

    protected String print(Object key) {
        if(key instanceof Object[]) {
            return Arrays.toString((Object[]) key);
        } else {
            return key.toString();
        }
    }

    public JsonNode get(Object key) {
        if(key instanceof Object[]) {
            return getWithArray((Object[]) key);
        }
        if(key instanceof JsonPointer) {
            return getWithPointer((JsonPointer) key);
        }
        if(key instanceof CharSequence) {
            return getWithChars((CharSequence) key);
        }
        throw new IllegalArgumentException(Objects.toString(key));
    }

    protected IllegalArgumentException createException(Object key, JsonNodeType type) {
        return new IllegalArgumentException("node type: " + type + " (key: " + print(key) + ")");
    }

    public boolean getBoolean(Object key) {
        JsonNode node = get(key);
        if(node.isBoolean()) {
            return node.booleanValue();
        } else {
            throw createException(key, node.getNodeType());
        }
    }

    public int getInt(Object key) {
        JsonNode node = get(key);
        if(node.isNumber()) {
            return node.intValue();
        } else {
            throw createException(key, node.getNodeType());
        }
    }

    public double getDouble(Object key) {
        JsonNode node = get(key);
        if(node.isNumber()) {
            return node.doubleValue();
        } else {
            throw createException(key, node.getNodeType());
        }
    }

    public String getString(Object key) {
        JsonNode node = get(key);
        if(node.isTextual()) {
            return node.textValue();
        } else {
            throw createException(key, node.getNodeType());
        }
    }

    public String getFormattedString(Object key, Object... args) {
        String pattern = getString(key);
        return MessageFormat.format(pattern, args);
    }
}
