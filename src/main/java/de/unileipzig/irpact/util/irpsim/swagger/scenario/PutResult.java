package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
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

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "id=" + getId() +
                '}';
    }
}
