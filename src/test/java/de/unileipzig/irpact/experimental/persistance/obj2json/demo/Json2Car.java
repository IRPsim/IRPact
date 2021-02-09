package de.unileipzig.irpact.experimental.persistance.obj2json.demo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.core.persistence.obj2json.ToObjectMapper;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class Json2Car implements ToObjectMapper {

    @Override
    public Car initalize(long uid, ObjectNode node) {
        Car car = new Car();
        car.setValue(node.get("value").doubleValue());
        car.setUID(uid);
        return car;
    }

    @Override
    public void finalize(Map<Long, Persistable> cache, ObjectNode node, Persistable persistable) {
    }
}
