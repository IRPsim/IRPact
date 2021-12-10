package de.unileipzig.irpact.core.persistence.binaryjson2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.CollectionSupplier;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapJsonNodeFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapStringFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapSupplier;
import de.unileipzig.irpact.core.persistence.binaryjson2.persist.PersistHelper;
import de.unileipzig.irpact.core.persistence.binaryjson2.restore.RestoreHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class IterableArrayNode implements Iterable<JsonNode> {

    protected ArrayNode root;
    protected int index = 0;

    public IterableArrayNode(ObjectMapper mapper) {
        this(mapper.createArrayNode());
    }

    public IterableArrayNode(JsonNodeCreator creator) {
        this(creator.arrayNode());
    }

    public IterableArrayNode(ArrayNode root) {
        this.root = root;
    }

    //=========================
    //access
    //=========================

    @Override
    public Iterator<JsonNode> iterator() {
        return root.iterator();
    }

    public ArrayNode getRoot() {
        return root;
    }

    public void clear() {
        resetIndex();
        root.removeAll();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void resetIndex() {
        index = 0;
    }

    public boolean hasNext() {
        return index < root.size();
    }

    public JsonNode peek() {
        return root.get(index);
    }

    public JsonNode next() {
        return root.get(index++);
    }

    //=========================
    //add special
    //=========================

    public void setClassId(PersistHelper helper, Class<?> c) {
        long id = helper.getClassId(c);
        add(id);
    }

    public long getClassId() {
        return getClassId(root);
    }

    public static long getClassId(ArrayNode arr) {
        return arr.get(0).longValue();
    }

    //=========================
    //add
    //=========================

    public void addNull() {
        root.addNull();
        index++;
    }

    public boolean peekNull() {
        return root.get(index).isNull();
    }

    public void add(int value) {
        root.add(value);
        index++;
    }

    public boolean peekInt() {
        return peek().isInt();
    }

    public void add(long value) {
        root.add(value);
        index++;
    }

    public boolean peekLong() {
        return peek().isLong();
    }

    public void add(double value) {
        root.add(value);
        index++;
    }

    public boolean peekDouble() {
        return peek().isDouble();
    }

    public void add(boolean value) {
        root.add(value);
        index++;
    }

    public boolean peekBoolean() {
        return peek().isBoolean();
    }

    public void add(String value) {
        root.add(value);
        index++;
    }

    public boolean peekText() {
        return peek().isTextual();
    }

    protected void addCollDirect(PersistHelper helper, Collection<?> c) throws Throwable {
        for(Object obj: c) {
            if(obj == null) {
                addNull();
            }
            else if(obj instanceof Integer) {
                add((Integer) obj);
            }
            else if(obj instanceof Long) {
                add((Long) obj);
            }
            else if(obj instanceof Double) {
                add((Double) obj);
            }
            else if(obj instanceof String) {
                add((String) obj);
            }
            else if(obj instanceof Boolean) {
                add((Boolean) obj);
            }
            else if(obj instanceof Collection) {
                addColl(helper, (Collection<?>) obj);
            }
            else if(obj instanceof Map) {
                addMap(helper, (Map<?, ?>) obj);
            }
            else {
                add(helper.getUid(obj));
            }
        }
    }

    public void addColl(PersistHelper helper, Collection<?> c) throws Throwable {
        IterableArrayNode sub = new IterableArrayNode(root.addArray());
        sub.addCollDirect(helper, c);
        index++;
    }

    public boolean peekColl() {
        return peek().isArray();
    }

    protected void addMap(ObjectNode mNode, PersistHelper helper, Map<?, ?> m) throws Throwable {
        for(Map.Entry<?, ?> e: m.entrySet()) {
            Object keyObj = e.getKey();
            if(keyObj == null) {
                throw new NullPointerException("key is null");
            }

            String keyStr;
            if(keyObj instanceof Number || keyObj instanceof String || keyObj instanceof Boolean) {
                keyStr = keyObj.toString();
            } else {
                throw new IllegalArgumentException("unsupported key: " + keyObj.getClass());
            }

            Object valueObj = e.getValue();
            if(valueObj == null) {
                mNode.putNull(keyStr);
            }
            else if(valueObj instanceof Integer) {
                mNode.put(keyStr, (Integer) valueObj);
            }
            else if(valueObj instanceof Long) {
                mNode.put(keyStr, (Long) valueObj);
            }
            else if(valueObj instanceof Double) {
                mNode.put(keyStr, (Double) valueObj);
            }
            else if(valueObj instanceof String) {
                mNode.put(keyStr, (String) valueObj);
            }
            else if(valueObj instanceof Boolean) {
                mNode.put(keyStr, (Boolean) valueObj);
            }
            else if(valueObj instanceof Collection) {
                IterableArrayNode sub = new IterableArrayNode(mNode.putArray(keyStr));
                sub.addCollDirect(helper, (Collection<?>) valueObj);
            }
            else if(valueObj instanceof Map) {
                ObjectNode sub = mNode.putObject(keyStr);
                addMap(sub, helper, (Map<?, ?>) valueObj);
            }
            else {
                long uid = helper.getUid(valueObj);
                mNode.put(keyStr, uid);
            }
        }
    }

    public void addMap(PersistHelper helper, Map<?, ?> m) throws Throwable {
        ObjectNode mNode = root.addObject();
        addMap(mNode, helper, m);
        index++;
    }

    public boolean peekMap() {
        return peek().isObject();
    }

    //=========================
    //get
    //=========================

//    public <E> Collection<E> getCol(
//            Supplier<? extends Collection<E>> supplier,
//            Function<? super JsonNode, ? extends E> mapper) {
//        if(peekColl()) {
//            ArrayNode arr = (ArrayNode) next();
//            Collection<E> c = supplier.get();
//            for(int i = 0; i < arr.size(); i++) {
//                c.add(
//                        mapper.apply(arr.get(i))
//                );
//            }
//            return c;
//        } else {
//            throw new IllegalStateException("no array node");
//        }
//    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Collection getCol(
            CollectionSupplier collSupplier,
            MapJsonNodeFunction mapper) {
        if(peekColl()) {
            ArrayNode arr = (ArrayNode) next();
            Collection c = collSupplier.newCollection();
            for(int i = 0; i < arr.size(); i++) {
                c.add(mapper.fromJson(arr.get(i)));
            }
            return c;
        } else {
            throw new IllegalStateException("no array node");
        }
    }

//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public <C, E> C getColOfCol(
//            Supplier<? extends List<?>> supplier1,
//            Supplier<? extends List<?>> supplier2,
//            Function<? super JsonNode, ? extends E> mapper) {
//        if(peekColl()) {
//            ArrayNode arr = (ArrayNode) next();
//            Collection c = supplier1.get();
//            for(int i = 0; i < arr.size(); i++) {
//                ArrayNode arr2 = (ArrayNode) arr.get(i);
//                Collection c2 = supplier2.get();
//                c.add(c2);
//                for(int j = 0; j < arr2.size(); j++) {
//                    c2.add(
//                            mapper.apply(arr2.get(j))
//                    );
//                }
//            }
//            return (C) c;
//        } else {
//            throw new IllegalStateException("no array node");
//        }
//    }

//    public <K, V> Map<K, V> getMap(
//            Supplier<? extends Map<K, V>> supplier,
//            Function<? super String, ? extends K> kMapper,
//            Function<? super JsonNode, ? extends V> vMapper) {
//        if(peekMap()) {
//            ObjectNode next = (ObjectNode) next();
//            Map<K, V> m = supplier.get();
//            Iterator<String> fields = next.fieldNames();
//            while(fields.hasNext()) {
//                String field = fields.next();
//                JsonNode value = next.get(field);
//                m.put(
//                        kMapper.apply(field),
//                        vMapper.apply(value)
//                );
//            }
//            return m;
//        } else {
//            throw new IllegalStateException("no object node");
//        }
//    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map getMap(
            MapSupplier mapSupplier,
            MapStringFunction keyMapper,
            MapJsonNodeFunction valueMapper) {
        if(peekMap()) {
            ObjectNode node = (ObjectNode) next();
            Map m = mapSupplier.newMap();
            Iterator<String> fields = node.fieldNames();
            while(fields.hasNext()) {
                String field = fields.next();
                Object key = keyMapper.fromString(field);
                Object value = valueMapper.fromJson(node.get(field));
                m.put(key, value);
            }
            return m;
        } else {
            throw new IllegalStateException("no map node");
        }
    }
}
