package de.unileipzig.irpact.input.def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/// description: Gruppe von Konsumenten
/// type: String
/// identifier: Gruppe von Konsumenten
/// unit:
/// domain:
/// validation:
/// hidden:
/// processing:
public class ConsumerAgentGroup {

    /// identifer:
    public String $name;

    /// description: Informationskompetenz
    /// type: float
    /// identifier:
    /// unit:
    /// domain: [0,1]
    /// validation:
    /// hidden:
    /// processing:
    public double informationAuthority;

    /// description: Attributgruppenzugehörigkeit
    /// type: Boolean
    /// identifier: Attributgruppenzugehörigkeit
    /// unit:
    /// domain:
    /// validation:
    /// hidden:
    /// processing:
    public ConsumerAgentGroupAttribute[] consumerAgentGroupAttributes;

    // end_of_definition

    public void readFrom(JsonNode node) {
        $name = node.get("$name").textValue();
        informationAuthority = node.get("informationAuthority").doubleValue();
        JsonNode attrNode = node.get("ConsumerAgentGroupAttribute");
        consumerAgentGroupAttributes = new ConsumerAgentGroupAttribute[attrNode.size()];
        for(int i = 0; i < attrNode.size(); i++) {
            JsonNode child = attrNode.get(i);
            ConsumerAgentGroupAttribute attr = new ConsumerAgentGroupAttribute();
            attr.readFrom(child);
            consumerAgentGroupAttributes[i] = attr;
        }
    }

    public void writeTo(JsonNode node) {
        ObjectNode objNode = (ObjectNode) node;
        objNode.put("$name", $name);
        objNode.put("informationAuthority", informationAuthority);
        ArrayNode attrNode = objNode.putArray("ConsumerAgentGroupAttribute");
        for(ConsumerAgentGroupAttribute attr: consumerAgentGroupAttributes) {
            ObjectNode child = attrNode.addObject();
            attr.writeTo(child);
        }
    }
}
