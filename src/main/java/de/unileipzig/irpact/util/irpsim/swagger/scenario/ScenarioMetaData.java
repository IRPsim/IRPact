package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

import java.util.Objects;

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

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }
    public String getIdString() {
        return String.valueOf(getId());
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

    public boolean isDeletable() {
        return JsonUtil.getBoolean(root, "deletable", false);
    }

    public long getDate() {
        return JsonUtil.getLong(root, "date", -1L);
    }

    public boolean isEqualsId(ScenarioMetaData other) {
        return getId() == other.getId();
    }

    public boolean isEqualsData(ScenarioMetaData other) {
        return getId() == other.getId()
                && Objects.equals(getName(), other.getName())
                && Objects.equals(getCreator(), other.getCreator())
                && Objects.equals(getDescription(), other.getDescription());
    }

    public boolean isEqualsData(String name, String creator, String description) {
        return Objects.equals(getName(), name)
                && Objects.equals(getCreator(), creator)
                && Objects.equals(getDescription(), description);
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "id=" + getId() +
                '}';
    }
}
