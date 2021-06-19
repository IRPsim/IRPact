package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

/**
 * @author Daniel Abitz
 */
public class ScenarioData extends Base {

    public ScenarioData(JsonNode root) {
        super(root);
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }
}
