package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

/**
 * @author Daniel Abitz
 */
public class ScenarioData extends Base {

    protected int id;

    public ScenarioData(JsonNode root, int id) {
        super(root);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return Integer.toString(id);
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }
}
