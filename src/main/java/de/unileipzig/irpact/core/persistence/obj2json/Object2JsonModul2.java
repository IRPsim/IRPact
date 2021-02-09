package de.unileipzig.irpact.core.persistence.obj2json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;
import de.unileipzig.irpact.commons.persistence.SimpleUIDManager;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class Object2JsonModul2 extends Object2JsonModul {

    protected SimpleUIDManager classIdManager;
    protected ObjectNode outClasses;

    public Object2JsonModul2() {
    }

    public void setClassIdManager(SimpleUIDManager classIdManager) {
        this.classIdManager = classIdManager;
    }

    public void setOutputClassesNode(ObjectNode outClasses) {
        this.outClasses = outClasses;
    }

    @Override
    protected void appendIdAndClass(Persistable persistable, ObjectNode node) throws PersistenceException {
        if(node.has(classField)) {
            throw new PersistenceException("CLASS field already exists");
        }
        String className = persistable.getClass().getName();
        long classId;
        if(outClasses.has(className)) {
            classId = outClasses.get(className).longValue();
        } else {
            classId = classIdManager.getUID();
            outClasses.put(className, classId);
        }
        node.put(classField, classId);
    }

    @Override
    protected Class<?> toClass(ObjectNode node) throws PersistenceException {
        if(!node.has(classField)) {
            throw new PersistenceException("CLASS field already exists");
        }
        long classId = node.get(classField).longValue();

        String className = null;
        Iterator<Map.Entry<String, JsonNode>> iter = outClasses.fields();
        while(iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            if(classId == entry.getValue().longValue()) {
                className = entry.getKey();
                break;
            }
        }
        if(className == null) {
            throw new PersistenceException("class with id '" + classId + "' not found");
        }

        return toClass(className);
    }
}
