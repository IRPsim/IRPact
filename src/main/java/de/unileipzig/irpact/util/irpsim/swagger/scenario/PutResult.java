package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

/**
 * @author Daniel Abitz
 */
public class PutResult extends Base {

    public PutResult(JsonNode root) {
        super(root);
    }

    public boolean hasId() {
        return getId() != -1;
    }

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    public JsonNode getMessages() {
        JsonNode node = getRootAsObject().get("messages");
        return node == null ? MissingNode.getInstance() : node;
    }

    public JsonNode getMessage() {
        JsonNode node = getMessages().get(0);
        return node == null ? MissingNode.getInstance() : node;
    }

    public JsonNode getSeverity() {
        JsonNode node = getMessage().get("severity");
        return node == null ? MissingNode.getInstance() : node;
    }

    public JsonNode getText() {
        JsonNode node = getMessage().get("text");
        return node == null ? MissingNode.getInstance() : node;
    }

    public String printText() {
        JsonNode node = getText();
        return node.isMissingNode() ? "MISSING_NODE" : node.textValue();
    }

    public boolean isError() {
        JsonNode severity = getSeverity();
        return severity.isTextual() && "ERROR".equals(severity.textValue());
    }

    @Override
    protected String printPrefix() {
        return "Scenario";
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "id=" + getId() +
                '}';
    }
}
