package de.unileipzig.irpact.experimental.persistance.obj2json.demo;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.core.persistence.obj2json.ToJsonMapper;

/**
 * @author Daniel Abitz
 */
public class Person2Json implements ToJsonMapper {

    @Override
    public ObjectNode toJson(JsonNodeCreator creator, Persistable obj) {
        Person p = (Person) obj;
        ObjectNode node = creator.objectNode();
        node.put("value", p.getValue());
        node.put("car", p.getCar().getUID());
        return node;
    }
}
