package de.unileipzig.irpact.util.irpsim.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;

/**
 * @author Daniel Abitz
 */
public abstract class Base {

    protected JsonNode root;

    public Base(JsonNode root) {
        this.root = root;
    }

    protected JsonNode getRoot() {
        return root;
    }

    protected ObjectNode getRootAsObject() {
        return getRootAs(ObjectNode.class);
    }

    protected ArrayNode getRootAsArray() {
        return getRootAs(ArrayNode.class);
    }

    protected <R extends JsonNode> R getRootAs(Class<R> clazz) {
        return clazz.cast(root);
    }

    protected String printPrefix() {
        return null;
    }

    public String print() {
        String prefix = printPrefix();
        return prefix == null || prefix.isEmpty()
                ? JsonUtil.toMinimalString(root)
                : prefix + JsonUtil.toMinimalString(root);
    }

    @Override
    public String toString() {
        return print();
    }
}
