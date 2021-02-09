package de.unileipzig.irpact.experimental.persistance.obj2json.demo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.core.persistence.obj2json.ToObjectMapper;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class Json2Person implements ToObjectMapper {

    @Override
    public Person initalize(long uid, ObjectNode node) {
        Person p = new Person();
        p.setValue(node.get("value").doubleValue());
        p.setUID(uid);
        return p;
    }

    @Override
    public void finalize(Map<Long, Persistable> cache, ObjectNode node, Persistable persistable) {
        Person p = (Person) persistable;
        long carId = node.get("car").longValue();
        Car c = (Car) cache.get(carId);
        p.setCar(c);
    }
}
