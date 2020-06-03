package de.unileipzig.irpact.OLD.io.def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.OLD.io.Constants;

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
    public String _name;

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
        _name = node.get(Constants.NAME).textValue();
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
        objNode.put(Constants.NAME, _name);
        objNode.put("informationAuthority", informationAuthority);
        ArrayNode attrNode = objNode.putArray("ConsumerAgentGroupAttribute");
        for(ConsumerAgentGroupAttribute attr: consumerAgentGroupAttributes) {
            ObjectNode child = attrNode.addObject();
            attr.writeTo(child);
        }
    }
}
