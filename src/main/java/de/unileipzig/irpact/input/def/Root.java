package de.unileipzig.irpact.input.def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Spezielles Root-Element, welches das Root-Element einer Json-Dateien repraesentiert.
 * Wird nicht mit geparst.
 *
 * @author Daniel Abitz
 */
public class Root {

    public ConsumerAgentGroup[] consumerAgentGroups;

    // end_of_definition

    public void readFrom(JsonNode node) {
        JsonNode groupNode = node.get("ConsumerAgentGroup");
        consumerAgentGroups = new ConsumerAgentGroup[groupNode.size()];
        for(int i = 0; i < groupNode.size(); i++) {
            JsonNode child = groupNode.get(i);
            ConsumerAgentGroup group = new ConsumerAgentGroup();
            group.readFrom(child);
            consumerAgentGroups[i] = group;
        }
    }

    public void writeTo(JsonNode node) {
        ObjectNode objNode = (ObjectNode) node;
        ArrayNode attrNode = objNode.putArray("ConsumerAgentGroup");
        for(ConsumerAgentGroup attr: consumerAgentGroups) {
            ObjectNode child = attrNode.addObject();
            attr.writeTo(child);
        }
    }
}
