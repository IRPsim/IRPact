package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

/**
 * @author Daniel Abitz
 */
public class ScenarioMetaData extends Base {

    public ScenarioMetaData() {
        super(JsonUtil.JSON.createObjectNode());
    }

    public ScenarioMetaData(JsonNodeCreator creator) {
        super(creator.objectNode());
    }

    public ScenarioMetaData(JsonNode root) {
        super(root);
    }

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }

    public String getName() {
        return JsonUtil.getText(root, "name", null);
    }
    public void setName(String name) {
        getRootAsObject().put("name", name);
    }

    public String getCreator() {
        return JsonUtil.getText(root, "creator", null);
    }
    public void setCreator(String creator) {
        getRootAsObject().put("creator", creator);
    }

    public String getDescription() {
        return JsonUtil.getText(root, "description", null);
    }
    public void setDescription(String description) {
        getRootAsObject().put("description", description);
    }

    public int getModeldefinition() {
        return JsonUtil.getInt(root, "modeldefinition", -1);
    }

    public long getDate() {
        return JsonUtil.getLong(root, "date", -1L);
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "id=" + getId() +
                '}';
    }
}
