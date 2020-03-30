package de.unileipzig.irpact.input.def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/// description: Gruppe von Konsumentenattributen
/// type: String
/// identifier: Gruppe von Konsumentenattributen
/// unit:
/// domain:
/// validation:
/// hidden:
/// processing:
public class ConsumerAgentGroupAttribute {

    /// identifer:
    public String $name;

    /// description: Testwert 1
    /// type: float
    /// identifier: Testwert 1
    /// unit:
    /// domain: [0,1]
    /// validation:
    /// hidden:
    /// processing:
    public double value0;

    /// description: Testwert 2
    /// type: float
    /// identifier: Testwert 2
    /// unit:
    /// domain: [10,20]
    /// validation:
    /// hidden:
    /// processing:
    public double value1;

    // end_of_definition

    public void readFrom(JsonNode node) {
        $name = node.get("$name").textValue();
        value0 = node.get("value0").doubleValue();
        value1 = node.get("value1").doubleValue();
    }

    public void writeTo(JsonNode node) {
        ObjectNode objNode = (ObjectNode) node;
        objNode.put("$name", $name);
        objNode.put("value0", value0);
        objNode.put("value1", value1);
    }
}
