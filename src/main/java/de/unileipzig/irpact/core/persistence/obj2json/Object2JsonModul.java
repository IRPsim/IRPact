package de.unileipzig.irpact.core.persistence.obj2json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;
import de.unileipzig.irpact.core.persistence.PersistenceModul;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class Object2JsonModul implements PersistenceModul {

    public static final String UID_FIELD = "_UID_";
    public static final String CLASS_FIELD = "_CLASS_";

    public static final String UID_FIELD_SHORT = "I";
    public static final String CLASS_FIELD_SHORT = "C";

    protected String uidField = UID_FIELD;
    protected String classField = CLASS_FIELD;

    protected MapperManager manager;
    protected ObjectNode out;
    protected ClassLoader loader;

    public Object2JsonModul() {
    }

    public void setManager(MapperManager manager) {
        this.manager = manager;
    }

    public void setOutputNode(ObjectNode out) {
        this.out = out;
    }

    public ObjectNode getOutputNode() {
        return out;
    }

    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public void setUIDField(String uidField) {
        this.uidField = uidField;
    }

    public void setClassField(String classField) {
        this.classField = classField;
    }

    @Override
    public void persist(Persistable persistable) throws PersistenceException {
        if(out == null) {
            throw new PersistenceException("no output");
        }
        if(persistable == null) {
            throw new PersistenceException("persistable is null");
        }
        String uid = persistable.getUIDString();
        if(out.has(uid)) {
            throw new PersistenceException("id '" + uid + "' already stored");
        }
        ObjectNode node = manager.toJson(out, persistable);
        appendIdAndClass(persistable, node);
        out.set(uid, node);
    }

    protected void appendIdAndClass(Persistable persistable, ObjectNode node) throws PersistenceException {
        if(node.has(uidField)) {
            throw new PersistenceException("UID field already exists");
        }
        if(node.has(classField)) {
            throw new PersistenceException("CLASS field already exists");
        }
        node.put(uidField, persistable.getUID());
        node.put(classField, persistable.getClass().getName());
    }

    protected long getUID(ObjectNode node) throws PersistenceException {
        if(!node.has(uidField)) {
            throw new PersistenceException("UID field already exists");
        }
        return node.get(uidField).longValue();
    }

    protected String getClass(ObjectNode node) throws PersistenceException {
        if(!node.has(classField)) {
            throw new PersistenceException("CLASS field already exists");
        }
        return node.get(classField).textValue();
    }

    protected Class<?> toClass(String className) throws PersistenceException {
        try {
            if(loader == null) {
                return Class.forName(className);
            } else {
                return loader.loadClass(className);
            }
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(e);
        }
    }

    protected Class<?> toClass(ObjectNode node) throws PersistenceException {
        String className = getClass(node);
        return toClass(className);
    }

    public List<Persistable> restore(ObjectNode root) throws PersistenceException {
        Map<Long, Persistable> cache = initalizeAll(root);
        finalizeAll(cache, root);
        return new ArrayList<>(cache.values());
    }

    public Map<Long, Persistable> initalizeAll(ObjectNode root) throws PersistenceException {
        if(root == null) {
            throw new NullPointerException("root is null");
        }
        Map<Long, Persistable> cache = new HashMap<>();
        initalizeAll(root, cache);
        return cache;
    }

    public boolean initalizeAll(ObjectNode root, Map<Long, Persistable> outCache) throws PersistenceException {
        if(root == null) {
            throw new NullPointerException("root is null");
        }
        boolean changed = false;
        Iterator<Map.Entry<String, JsonNode>> iter = root.fields();
        while(iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String uidStr = entry.getKey();
            long uid = Long.parseLong(uidStr);
            if(outCache.containsKey(uid)) {
                throw new PersistenceException("uid '" + uid + "' already exists");
            }

            ObjectNode node = (ObjectNode) entry.getValue();
            Class<?> c = toClass(node);
            Persistable p = manager.initalizePersistable(uid, c, node);
            outCache.put(uid, p);
            changed = true;
        }
        return changed;
    }

    public void finalizeAll(Map<Long, Persistable> cache, ObjectNode root) throws PersistenceException {
        for(Persistable p: cache.values()) {
            String uidStr = p.getUIDString();
            ObjectNode node = (ObjectNode) root.get(uidStr);
            if(node == null) {
                throw new PersistenceException("id '" + uidStr + "' not found");
            }
            manager.finalizePersistable(cache, node, p);
        }
    }
}
