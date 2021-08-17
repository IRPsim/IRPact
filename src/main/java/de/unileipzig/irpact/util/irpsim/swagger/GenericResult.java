package de.unileipzig.irpact.util.irpsim.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;

/**
 * @author Daniel Abitz
 */
public class GenericResult extends Base {

    public GenericResult(JsonNode root) {
        super(root);
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean hasId() {
        if(root == null) return false;
        if(root.has("id")) return true;
        if(root.isArray() && root.size() == 1) return true;
        return false;
    }

    public int getId() {
        if(hasId()) {
            if(root.has("id")) {
                return root.get("id").intValue();
            }
            if(root.isArray() && root.size() == 1) {
                return root.get(0).intValue();
            }
        }
        return -1;
    }

    public boolean isBoolean() {
        return root != null && root.isBoolean();
    }

    public boolean isFalse() {
        return isBoolean() && !root.booleanValue();
    }

    public boolean isTrue() {
        return isBoolean() && root.booleanValue();
    }

    public boolean hasMessages() {
        return root != null && root.has("messages");
    }

    public JsonNode getMessagesNode() {
        return root == null ? null : root.get("messages");
    }

    public boolean isError() {
        JsonNode messages = getMessagesNode();
        if(messages == null || messages.isEmpty()) return false;
        JsonNode firstChild = messages.get(0);
        if(firstChild == null) return false;
        JsonNode severityNode = firstChild.get("severity");
        if(severityNode == null || !severityNode.isTextual()) return false;
        return "ERROR".equals(severityNode.textValue());
    }

    public String getErrorMessage() {
        JsonNode messages = getMessagesNode();
        if(messages == null || messages.isEmpty()) return null;
        JsonNode firstChild = messages.get(0);
        if(firstChild == null) return null;
        JsonNode textNode = firstChild.get("severity");
        if(textNode == null || !textNode.isTextual()) return null;
        return textNode.textValue();
    }

    public boolean isNull() {
        return root == null || root.isNull();
    }

    public boolean isNotNull() {
        return !isNull();
    }

    @Override
    public JsonNode getRoot() {
        return super.getRoot();
    }

    public boolean isObject() {
        return root != null && root.isObject();
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    public boolean isArray() {
        return root != null && root.isArray();
    }

    @Override
    public ArrayNode getRootAsArray() {
        return super.getRootAsArray();
    }

    @Override
    protected String printPrefix() {
        return "GenericResult";
    }

    @Override
    public String print() {
        if(isBoolean()) {
            return printPrefix() + "#" + JsonUtil.toMinimalString(root);
        } else {
            return super.print();
        }
    }
}
