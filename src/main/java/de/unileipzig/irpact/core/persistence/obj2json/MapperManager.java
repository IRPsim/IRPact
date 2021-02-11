package de.unileipzig.irpact.core.persistence.obj2json;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class MapperManager {

    protected JsonNodeCreator creator = Util.JSON_MAPPER.getNodeFactory();
    protected Map<Class<?>, ToJsonMapper> toJsonMappers;
    protected Map<Class<?>, ToObjectMapper> toObjectMappers;

    public MapperManager() {
        this(new HashMap<>(), new HashMap<>());
    }

    public MapperManager(Map<Class<?>, ToJsonMapper> toJsonMappers, Map<Class<?>, ToObjectMapper> toObjectMappers) {
        this.toJsonMappers = toJsonMappers;
        this.toObjectMappers = toObjectMappers;
    }

    public void setCreator(JsonNodeCreator creator) {
        this.creator = creator;
    }

    public JsonNodeCreator getCreator() {
        return creator;
    }

    public void register(Class<?> c, ToJsonMapper toJson, ToObjectMapper toObject) {
        toJsonMappers.put(c, toJson);
        toObjectMappers.put(c, toObject);
    }

    public ObjectNode toJson(Persistable persistable) throws PersistenceException {
        return toJson(creator, persistable);
    }

    public ObjectNode toJson(JsonNodeCreator creator, Persistable persistable) throws PersistenceException {
        if(persistable == null) {
            throw new PersistenceException("persistable is null");
        }
        ToJsonMapper m = toJsonMappers.get(persistable.getClass());
        if(m == null) {
            throw new PersistenceException("no mapper found for '" + persistable.getClass() + "'");
        }
        return m.toJson(creator, persistable);
    }

    public Persistable initalizePersistable(long uid, Class<?> c, ObjectNode node) throws PersistenceException {
        ToObjectMapper m = toObjectMappers.get(c);
        if(m == null) {
            throw new PersistenceException("no mapper found for '" + c + "'");
        }
        return m.initalize(uid, node);
    }

    public void finalizePersistable(Map<Long, Persistable> cache, ObjectNode node, Persistable persistable) throws PersistenceException {
        ToObjectMapper m = toObjectMappers.get(persistable.getClass());
        if(m == null) {
            throw new PersistenceException("no mapper found for '" + persistable.getClass() + "'");
        }
        m.finalize(cache, node, persistable);
    }
}
