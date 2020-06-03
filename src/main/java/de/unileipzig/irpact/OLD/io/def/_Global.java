package de.unileipzig.irpact.OLD.io.def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

//Global ist eine spezielle Definitionsklasse (daher der Unterstrich).
//In ihr werden globale/statische Variablen verwaltet.
//Diese Variablen entsprechen den Skalaren aus GAMS und werden in jene uebersetzt.
///
public class _Global {

    /// description: Multiplier
    /// type: float
    /// identifier: Multiplier
    /// unit:
    /// domain:
    /// validation:
    /// hidden:
    /// processing:
    public double multiplier;

    // end_of_definition

    public void readFrom(JsonNode node) {
        multiplier = node.get("multiplier").doubleValue();
    }

    public void writeTo(JsonNode node) {
        ObjectNode objNode = (ObjectNode) node;
        objNode.put("multiplier", multiplier);
    }
}
