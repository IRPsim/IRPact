package de.unileipzig.irpact.util.irpsim.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
}
