package de.unileipzig.irpact.OLD.io.def;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.OLD.io.Constants;

/**
 * Spezielles Root-Element, welches das Root-Element einer Json-Dateien repraesentiert.
 * Wird nicht mit geparst.
 *
 * Allgemeine Informationen:
 *
 * Klassen duerfen nicht mehrere typgleichen verschachtelte Elemente besitzen.
 * Zum Beispiel darf nicht "Wert[] values0" und "Wert[] values1" existieren.
 * In den Set-Elementen wuerden diese zusammen geworfen werden, so dass ein Rueckparsen nicht moeglich ist.
 *
 * @author Daniel Abitz
 */
//anmerkung: wenn par_MASTERTYPE_SLAVENAME genutzt wird (statt par_MASTERTYPE_SLAVETYPE), waere es moeglich - ueberlegen, ob diese variante nachteile haette
//... dann gibt es sehr wahrscheinlich probleme bei den rawsets

//idee2: einfach type+name kombinieren mittels spez. trennzeichen, dann koennte das sehr gut zurueck geparst werden
//doof: dann muesste dieser typ auch beim tree mit notiert werden, $type vllt?
//... obere variante ist besser
public class Root {

    public _Global global;

    public ConsumerAgentGroup[] consumerAgentGroups;

    public void readFrom(JsonNode node) {
        JsonNode globalNode = node.get(Constants.GLOBAL);
        if(globalNode != null) {
            global = new _Global();
            global.readFrom(globalNode);
        }

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
        if(global != null) {
            ObjectNode globalNode = objNode.putObject(Constants.GLOBAL);
            global.writeTo(globalNode);
        }
        ArrayNode attrNode = objNode.putArray("ConsumerAgentGroup");
        for(ConsumerAgentGroup attr: consumerAgentGroups) {
            ObjectNode child = attrNode.addObject();
            attr.writeTo(child);
        }
    }
}
