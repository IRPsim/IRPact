package de.unileipzig.irpact.jadex.persistance.binary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.util.*;
import de.unileipzig.irpact.commons.util.data.TripleMapping;
import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.jadex.persistance.JadexPersistable;
import de.unileipzig.irpact.jadex.persistance.binary.io.BinaryPersistJson;
import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.util.*;
import java.util.function.*;

/**
 * Stores data in a binary json (SMILE) format.
 *
 * @author Daniel Abitz
 */
public final class BinaryJsonData extends PersistableBase implements JadexPersistable {

    public static ToLongFunction<Integer> INT2LONG = Number::longValue;
    public static LongFunction<Integer> LONG2INT = l -> (int) l;

    public static final String UID_PREFIX = "x";

    public static final int NOTHING_ID = -1;
    private static final int UID_ID = 0;
    private static final int TYPE_ID = 1;
    private static final int FIRST_AUTO_ID = 2;

    protected int autoPutId = FIRST_AUTO_ID;
    protected int autoGetId = FIRST_AUTO_ID;
    protected ArrayNode root;

    protected boolean simulationMode;
    protected boolean putMode;

    private BinaryJsonData(ArrayNode root) {
        this.root = root;
        setSimulationMode(false);
        setPutMode();
    }

    public static BinaryJsonData restore(JsonNode restored) throws IOException {
        ArrayNode node = (ArrayNode) restored;
        BinaryJsonData bdata = new BinaryJsonData(node);
        long uid = bdata.ensureGetUid();
        bdata.setUID(uid);
        return bdata;
    }

    public static BinaryJsonData init(JsonNodeCreator creator, long uid, Class<?> c, ClassManager classManager) {
        BinaryJsonData data = new BinaryJsonData(creator.arrayNode());
        if(classManager != null && classManager.isEnabled()) {
            long classId = classManager.getId(c);
            data.init(uid, classId);
        } else {
            data.init(uid, c);
        }
        return data;
    }

    @Override
    public BinaryPersistData toPersistData() {
        return BinaryPersistJson.toData(root, UID_PREFIX, getUID());
    }

    public void setPutMode() {
        putMode = true;
    }

    public void setGetMode() {
        putMode = false;
    }

    private ArrayNode validateSet(int index) {
        if(root.size() != index) {
            throw new IllegalStateException("index mismatch: " + root.size() + " != " + index);
        }
        return root;
    }

    private JsonNode validateGet(int index) {
        if(root.size() < index) {
            throw new IllegalStateException("index not found");
        }
        return root.get(index);
    }

    private void init(long uid, Class<?> c) {
        if(isSimulationMode()) return;
        setUID(uid);
        validateSet(UID_ID).add(uid);
        validateSet(TYPE_ID).add(c.getName());
    }

    private void init(long uid, long classId) {
        if(isSimulationMode()) return;
        setUID(uid);
        validateSet(UID_ID).add(uid);
        validateSet(TYPE_ID).add(classId);
    }

    public long ensureGetUid() {
        checkSimulationMode();
        return validateGet(UID_ID).longValue();
    }

    public String ensureGetType(ClassManager manager) {
        checkSimulationMode();
        final String type;
        if(manager != null && manager.isEnabled()) {
            long classId = validateGet(TYPE_ID).longValue();
            type = manager.getClass(classId);
        } else {
            type = validateGet(TYPE_ID).textValue();
        }
        if(type == null || type.isEmpty()) {
            throw new NoSuchElementException("missing type");
        }
        return type;
    }

    public byte[] toBytes() throws IOException {
        checkSimulationMode();
        return IRPactJson.toBytesWithSmile(root);
    }

    public String printBytes(String prefix) {
        return BinaryPersistJson.print(getRoot(), prefix, getUID());
    }

    public String printMinimalJson() {
        return root.toString();
    }

    boolean isSimulationMode() {
        return simulationMode;
    }

    void setSimulationMode(boolean simulationMode) {
        this.simulationMode = simulationMode;
    }

    private void checkSimulationMode() {
        if(isSimulationMode()) {
            throw new IllegalStateException("simulation mode");
        }
    }

    public ArrayNode getRoot() {
        return root;
    }

    public Collection<String> getFields() {
        List<String> list = new ArrayList<>();
        for(String str: Util.iterateFieldNames(root)) {
            list.add(str);
        }
        return list;
    }

    private static ObjectNode computeObjectIfAbsent(ObjectNode root, String field) {
        if(root.has(field)) {
            return (ObjectNode) root.get(field);
        } else {
            return root.putObject(field);
        }
    }

    //=========================
    //auto put
    //=========================

    private void requiresPutMode() {
        if(!putMode) {
            throw new IllegalStateException("get mode");
        }
    }

    public void resetPut() {
        autoPutId = FIRST_AUTO_ID;
    }

    private int nextPutId() {
        requiresPutMode();
        return autoPutId++;
    }

    public void putBoolean(boolean value) {
        if(isSimulationMode()) return;
        validateSet(nextPutId()).add(value);
    }

    public void putInt(int value) {
        if(isSimulationMode()) return;
        validateSet(nextPutId()).add(value);
    }

    public void putNothing() {
        if(isSimulationMode()) return;
        putLong(NOTHING_ID);
    }

    public void putLong(long value) {
        if(isSimulationMode()) return;
        validateSet(nextPutId()).add(value);
    }

    public void putDouble(double value) {
        if(isSimulationMode()) return;
        validateSet(nextPutId()).add(value);
    }

    public void putText(String value) {
        if(isSimulationMode()) return;
        validateSet(nextPutId()).add(value);
    }

    public void putLongArray(long[] array) {
        if(isSimulationMode()) return;
        ArrayNode arr = validateSet(nextPutId()).addArray();
        for(long n: array) {
            arr.add(n);
        }
    }

    public void putLongMultiLongMap(Map<Long, List<Long>> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<Long, List<Long>> entry: map.entrySet()) {
            ArrayNode arr = obj.putArray(Long.toString(entry.getKey()));
            for(long n: entry.getValue()) {
                arr.add(n);
            }
        }
    }

    public void putLongMultiStringMap(Map<Long, List<String>> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<Long, List<String>> entry: map.entrySet()) {
            ArrayNode arr = obj.putArray(Long.toString(entry.getKey()));
            for(String n: entry.getValue()) {
                arr.add(n);
            }
        }
    }

    public void putLongLongMap(Map<Long, Long> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            obj.put(Long.toString(entry.getKey()), entry.getValue());
        }
    }

    public void putLongLongLongMap(Map<Long, Map<Long, Long>> table) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<Long, Map<Long, Long>> entry: table.entrySet()) {
            ObjectNode srcNode = obj.putObject(Long.toString(entry.getKey()));
            for(Map.Entry<Long, Long> entry0: entry.getValue().entrySet()) {
                srcNode.put(Long.toString(entry0.getKey()), entry0.getValue());
            }
        }
    }

    public void putLongDoubleMap(Map<Long, Double> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<Long, Double> entry: map.entrySet()) {
            obj.put(Long.toString(entry.getKey()), entry.getValue());
        }
    }

    public void putLongLongDoubleMap(Map<Long, Map<Long, Double>> table) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<Long, Map<Long, Double>> entry: table.entrySet()) {
            ObjectNode srcNode = obj.putObject(Long.toString(entry.getKey()));
            for(Map.Entry<Long, Double> entry0: entry.getValue().entrySet()) {
                srcNode.put(Long.toString(entry0.getKey()), entry0.getValue());
            }
        }
    }

    //=========================
    //auto get
    //=========================

    private void requiresGetMode() {
        if(putMode) {
            throw new IllegalStateException("put mode");
        }
    }

    public void resetGet() {
        autoGetId = FIRST_AUTO_ID;
    }

    private int nextGetId() {
        requiresGetMode();
        return autoGetId++;
    }

    private JsonNode nextNode() {
        return nextNode(false);
    }

    private JsonNode nextNodeAllowNull() {
        return nextNode(true);
    }

    private JsonNode nextNode(boolean allowNull) {
        int id = nextGetId();
        return get(id, allowNull);
    }

    private JsonNode peekNode() {
        requiresGetMode();
        return get(autoGetId, false);
    }

    private JsonNode get(int id, boolean allowNull) {
        JsonNode node = validateGet(id);
        if(node == null || node.isMissingNode() || (!allowNull && node.isNull())) {
            String type = node == null
                    ? "null"
                    : node.getNodeType().toString();
            throw new IllegalStateException("missing node: " + id + " (type: " + type + ")");
        }
        return node;
    }

    public boolean getBoolean() {
        checkSimulationMode();
        return nextNode().booleanValue();
    }

    public int getInt() {
        checkSimulationMode();
        return nextNode().intValue();
    }

    public long getLong() {
        checkSimulationMode();
        return nextNode().longValue();
    }

    public boolean peekNothing() {
        checkSimulationMode();
        return peekNode().longValue() == NOTHING_ID;
    }

    public double getDouble() {
        checkSimulationMode();
        return nextNode().doubleValue();
    }

    public String getText() {
        checkSimulationMode();
        return nextNode().textValue();
    }

    public String getTextOrNull() {
        checkSimulationMode();
        return nextNodeAllowNull().textValue();
    }

    public long[] getLongArray() {
        checkSimulationMode();
        ArrayNode arrNode = (ArrayNode) nextNode();
        long[] arr = new long[arrNode.size()];
        for(int i = 0; i < arrNode.size(); i++) {
            arr[i] = arrNode.get(i).longValue();
        }
        return arr;
    }

    public Map<Long, Map<Long, Double>> getLongLongDoubleTable() {
        checkSimulationMode();
        Map<Long, Map<Long, Double>> table = new LinkedHashMap<>();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            long srcId = Long.parseLong(entry.getKey());
            ObjectNode srcNode = (ObjectNode) entry.getValue();
            Map<Long, Double> map = table.computeIfAbsent(srcId, _srcId -> new LinkedHashMap<>());
            for(Map.Entry<String, JsonNode> srcEntry: Util.iterateFields(srcNode)) {
                long tarId = Long.parseLong(srcEntry.getKey());
                double value = srcEntry.getValue().doubleValue();
                map.put(tarId, value);
            }
        }
        return table;
    }

    public Map<Long, Long> getLongLongMap() {
        checkSimulationMode();
        Map<Long, Long> map = new LinkedHashMap<>();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            long srcId = Long.parseLong(entry.getKey());
            long tarId = entry.getValue().longValue();
            map.put(srcId, tarId);
        }
        return map;
    }

    public Map<Long, Map<Long, Long>> getLongLongLongTable() {
        checkSimulationMode();
        Map<Long, Map<Long, Long>> table = new LinkedHashMap<>();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            long srcId = Long.parseLong(entry.getKey());
            ObjectNode srcNode = (ObjectNode) entry.getValue();
            Map<Long, Long> map = table.computeIfAbsent(srcId, _srcId -> new LinkedHashMap<>());
            for(Map.Entry<String, JsonNode> srcEntry: Util.iterateFields(srcNode)) {
                long tarId = Long.parseLong(srcEntry.getKey());
                long value = srcEntry.getValue().longValue();
                map.put(tarId, value);
            }
        }
        return table;
    }

    public Map<Long, Double> getLongDoubleMap() {
        checkSimulationMode();
        Map<Long, Double> map = new LinkedHashMap<>();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            long key = Long.parseLong(entry.getKey());
            double value = entry.getValue().doubleValue();
            map.put(key, value);
        }
        return map;
    }

    public Map<Long, List<String>> getLongMultiStringMap() {
        checkSimulationMode();
        Map<Long, List<String>> map = new LinkedHashMap<>();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            long srcId = Long.parseLong(entry.getKey());
            ArrayNode tarNode = (ArrayNode) entry.getValue();
            List<String> tarList = map.computeIfAbsent(srcId, _srcId -> new ArrayList<>());
            for(JsonNode node: Util.iterateElements(tarNode)) {
                tarList.add(node.textValue());
            }
        }
        return map;
    }

    public Map<Long, List<Long>> getLongMultiLongMap() {
        checkSimulationMode();
        Map<Long, List<Long>> map = new LinkedHashMap<>();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            long srcId = Long.parseLong(entry.getKey());
            ArrayNode tarNode = (ArrayNode) entry.getValue();
            List<Long> tarList = map.computeIfAbsent(srcId, _srcId -> new ArrayList<>());
            for(JsonNode node: Util.iterateElements(tarNode)) {
                tarList.add(node.longValue());
            }
        }
        return map;
    }

    //=========================
    //set
    //=========================

    public static <T> ToLongFunction<T> ensureGetUID(PersistManager manager) {
        return obj -> {
            try {
                return manager.ensureGetUID(obj);
            } catch (PersistException e) {
                throw new UncheckedPersistException(e);
            }
        };
    }

    public static <K> Map<Long, Double> mapToLongDoubleMap(
            Map<K, ? extends Number> input,
            PersistManager manager) {
        return mapToLongDoubleMap(
                input,
                ensureGetUID(manager),
                Number::doubleValue
        );
    }

    public static <K, V> Map<Long, Double> mapToLongDoubleMap(
            Map<K, V> input,
            ToLongFunction<K> keyToLong,
            ToDoubleFunction<V> valueToDouble) {
        Map<Long, Double> idMap = new LinkedHashMap<>();

        for(Map.Entry<K, V> entry: input.entrySet()) {
            long id = keyToLong.applyAsLong(entry.getKey());
            double value = valueToDouble.applyAsDouble(entry.getValue());
            if(idMap.containsKey(id)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "id {} already exists", id);
            }
            idMap.put(id, value);
        }

        return idMap;
    }

    public static <K, V> Map<Long, Long> mapToLongLongMap(
            Map<K, V> input,
            PersistManager manager) {
        return mapToLongLongMap(
                input,
                ensureGetUID(manager),
                ensureGetUID(manager)
        );
    }

    public static <K, V> Map<Long, Long> mapToLongLongMap(
            Map<K, V> input,
            ToLongFunction<K> keyToLong,
            ToLongFunction<V> valueToLong) {
        Map<Long, Long> idMap = new LinkedHashMap<>();

        for(Map.Entry<K, V> entry: input.entrySet()) {
            long id = keyToLong.applyAsLong(entry.getKey());
            long value = valueToLong.applyAsLong(entry.getValue());
            if(idMap.containsKey(id)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "id {} already exists", id);
            }
            idMap.put(id, value);
        }

        return idMap;
    }

    public static <K, V> Map<Long, Long> mapToLongLongMap(
            Collection<? extends K> keys,
            Function<? super K, ? extends V> valueFunc,
            ToLongFunction<K> keyToLong,
            ToLongFunction<V> valueToLong) {
        Map<Long, Long> idMap = new LinkedHashMap<>();

        for(K key: keys) {
            V value = valueFunc.apply(key);
            long id = keyToLong.applyAsLong(key);
            long valueId = valueToLong.applyAsLong(value);
            if(idMap.containsKey(id)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "id {} already exists", id);
            }
            idMap.put(id, valueId);
        }

        return idMap;
    }

    public <A, B, C> Map<Long, Map<Long, Long>> mapToLongLongLongMap(
            TripleMapping<A, B, C> mapping,
            ToLongFunction<A> aToLong,
            ToLongFunction<B> bToLong,
            ToLongFunction<C> cToLong) {
        Map<Long, Map<Long, Long>> idMap = new LinkedHashMap<>();
        for(A a: mapping.iterableA()) {
            long aId = aToLong.applyAsLong(a);
            Map<Long, Long> map0 = idMap.computeIfAbsent(aId, _aId -> new LinkedHashMap<>());
            for(Map.Entry<B, C> bc: mapping.iterableBC(a)) {
                long bId = bToLong.applyAsLong(bc.getKey());
                long cId = cToLong.applyAsLong(bc.getValue());
                map0.put(bId, cId);
            }
        }
        return idMap;
    }

    public <A, B, C> Map<Long, Map<Long, Double>> mapToLongLongLongMap(
            TripleMapping<A, B, C> mapping,
            ToLongFunction<A> aToLong,
            ToLongFunction<B> bToLong,
            ToDoubleFunction<C> cToLong) {
        Map<Long, Map<Long, Double>> idMap = new LinkedHashMap<>();
        for(A a: mapping.iterableA()) {
            long aId = aToLong.applyAsLong(a);
            Map<Long, Double> map0 = idMap.computeIfAbsent(aId, _aId -> new LinkedHashMap<>());
            for(Map.Entry<B, C> bc: mapping.iterableBC(a)) {
                long bId = bToLong.applyAsLong(bc.getKey());
                double cValue = cToLong.applyAsDouble(bc.getValue());
                map0.put(bId, cValue);
            }
        }
        return idMap;
    }

    //=========================
    //get
    //=========================

    public static <R> LongFunction<R> ensureGet(RestoreManager manager) throws UncheckedRestoreException {
        return uid -> {
            try {
                return manager.ensureGet(uid);
            } catch (RestoreException e) {
                throw new UncheckedRestoreException(e);
            }
        };
    }

    public static <K, V> Map<K, V> mapFromLongDoubleMap(
            Map<Long, Double> input,
            LongFunction<K> longToKey,
            DoubleFunction<V> doubleToValue) {
        Map<K, V> map = new LinkedHashMap<>();

        for(Map.Entry<Long, Double> entry: input.entrySet()) {
            K key = longToKey.apply(entry.getKey());
            V value = doubleToValue.apply(entry.getValue());
            if(map.containsKey(key)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "key already exists");
            }
            map.put(key, value);
        }

        return map;
    }

    public static <K, V> Map<K, V> mapFromLongLongMap(
            Map<Long, Long> input,
            LongFunction<K> longToKey,
            LongFunction<V> longToValue) {
        Map<K, V> map = new LinkedHashMap<>();

        mapFromLongLongMap(input, longToKey, longToValue, map);

        return map;
    }

    public static <K, V> void mapFromLongLongMap(
            Map<Long, Long> input,
            RestoreManager manager,
            Map<K, V> target) {

        mapFromLongLongMap(
                input,
                ensureGet(manager),
                ensureGet(manager),
                target
        );
    }

    public static <K, V> void mapFromLongLongMap(
            Map<Long, Long> input,
            LongFunction<K> longToKey,
            LongFunction<V> longToValue,
            Map<K, V> target) {

        for(Map.Entry<Long, Long> entry: input.entrySet()) {
            K key = longToKey.apply(entry.getKey());
            V value = longToValue.apply(entry.getValue());
            if(target.containsKey(key)) {
                throw ExceptionUtil.create(IllegalArgumentException::new, "key already exists");
            }
            target.put(key, value);
        }
    }

    public <A, B, C> TripleMapping<A, B, C> mapToLongLongLongMap(
            Map<Long, Map<Long, Long>> idMapping,
            LongFunction<A> longToA,
            LongFunction<B> longToB,
            LongFunction<C> longToC,
            TripleMapping<A, B, C> target) {
        for(Long aId: idMapping.keySet()) {
            A a = longToA.apply(aId);
            for(Map.Entry<Long, Long> bcIds: idMapping.get(aId).entrySet()) {
                B b = longToB.apply(bcIds.getKey());
                C c = longToC.apply(bcIds.getValue());
                target.put(a, b, c);
            }
        }
        return target;
    }

    public <A, B, C> TripleMapping<A, B, C> mapToLongLongDoubleMap(
            Map<Long, Map<Long, Double>> idMapping,
            LongFunction<A> longToA,
            LongFunction<B> longToB,
            DoubleFunction<C> longToC,
            TripleMapping<A, B, C> target) {
        for(Long aId: idMapping.keySet()) {
            A a = longToA.apply(aId);
            for(Map.Entry<Long, Double> bcIds: idMapping.get(aId).entrySet()) {
                B b = longToB.apply(bcIds.getKey());
                C c = longToC.apply(bcIds.getValue());
                target.put(a, b, c);
            }
        }
        return target;
    }

    //==================================================
    //v2
    //==================================================

    public <A> void putIdValue(
            A value,
            ToLongFunction<A> toId) {
        if(isSimulationMode()) return;
        long id = toId.applyAsLong(value);
        putLong(id);
    }

    public <A> A getIdValue(LongFunction<A> fromId) {
        checkSimulationMode();
        long id = getLong();
        return fromId.apply(id);
    }

    public <A> void getIdValue(LongFunction<A> fromId, Consumer<A> consumer) {
        A a = getIdValue(fromId);
        consumer.accept(a);
    }

    public <A> void putCollection(
            Collection<A> coll,
            BiConsumer<ArrayNode, A> aApplier) {
        if(isSimulationMode()) return;
        ArrayNode arr = validateSet(nextPutId()).addArray();
        for(A a: coll) {
            aApplier.accept(arr, a);
        }
    }

    public <A> void getCollection(
            IndexFunction<ArrayNode, A> aFunc,
            Collection<A> out) {
        checkSimulationMode();
        ArrayNode arr = (ArrayNode) nextNode();
        for(int i = 0; i < arr.size(); i++) {
            A a = aFunc.apply(arr, i);
            out.add(a);
        }
    }

    public <A> void putIdCollection(
            Collection<A> coll,
            ToLongFunction<A> toId) {
        if(isSimulationMode()) return;
        ArrayNode arr = validateSet(nextPutId()).addArray();
        for(A a: coll) {
            long id = toId.applyAsLong(a);
            arr.add(id);
        }
    }

    public <A> void getIdCollection(
            LongFunction<A> fromId,
            Collection<A> out) {
        checkSimulationMode();
        ArrayNode arr = (ArrayNode) nextNode();
        for(int i = 0; i < arr.size(); i++) {
            long id = arr.get(i).longValue();
            A a = fromId.apply(id);
            out.add(a);
        }
    }

    public <A> void getIdCollection(
            LongFunction<A> fromId,
            Consumer<A> consumer) {
        checkSimulationMode();
        ArrayNode arr = (ArrayNode) nextNode();
        for(int i = 0; i < arr.size(); i++) {
            long id = arr.get(i).longValue();
            A a = fromId.apply(id);
            consumer.accept(a);
        }
    }

    public <A, B> void putMap(
            Map<A, B> map,
            ToLongFunction<A> aToLong,
            TriConsumer<ObjectNode, String, B> bApplier) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<A, B> entry: map.entrySet()) {
            long aLong = aToLong.applyAsLong(entry.getKey());
            bApplier.accept(obj, Long.toString(aLong), entry.getValue());
        }
    }

    public <A, B> void getMap(
            LongFunction<A> longToA,
            BiFunction<ObjectNode, String, B> bFunc,
            Map<A, B> out) {
        checkSimulationMode();
        ObjectNode obj = (ObjectNode) nextNode();
        for(String idStr: Util.iterateFieldNames(obj)) {
            long id = Long.parseLong(idStr);
            A a = longToA.apply(id);
            B b = bFunc.apply(obj, idStr);
            out.put(a, b);
        }
    }

    public <A, B, C> void putMapMap(
            Map<A, Map<B, C>> mapmap,
            ToLongFunction<A> aToLong,
            ToLongFunction<B> bToLong,
            TriConsumer<ObjectNode, String, C> cApplier) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Map.Entry<A, Map<B, C>> entry: mapmap.entrySet()) {
            long aLong = aToLong.applyAsLong(entry.getKey());
            ObjectNode srcNode = obj.putObject(Long.toString(aLong));
            for(Map.Entry<B, C> entry0: entry.getValue().entrySet()) {
                long bLong = bToLong.applyAsLong(entry0.getKey());
                cApplier.accept(srcNode, Long.toString(bLong), entry0.getValue());
            }
        }
    }

    public <A, B, C> void getMapMap(
            LongFunction<A> longToA,
            LongFunction<B> longToB,
            BiFunction<ObjectNode, String, C> cFunc,
            Map<A, Map<B, C>> out,
            MapSupplier supplier) {
        checkSimulationMode();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            String aIdStr = entry.getKey();
            long aId = Long.parseLong(aIdStr);
            A a = longToA.apply(aId);

            Map<B, C> mapBC = out.computeIfAbsent(a, _a -> supplier.newMap());
            ObjectNode child = (ObjectNode) entry.getValue();
            for(String bIdStr: Util.iterateFieldNames(child)) {
                long bId = Long.parseLong(bIdStr);
                B b = longToB.apply(bId);
                C c = cFunc.apply(child, bIdStr);
                mapBC.put(b, c);
            }
        }
    }

    public <A, B, C> void putTriple(
            TripleMapping<A, B, C> map,
            ToLongFunction<A> aToLong,
            ToLongFunction<B> bToLong,
            TriConsumer<ObjectNode, String, C> cApplier) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(A a: map.iterableA()) {
            long aLong = aToLong.applyAsLong(a);
            ObjectNode srcNode = obj.putObject(Long.toString(aLong));
            for(Map.Entry<B, C> entry0: map.iterableBC(a)) {
                long bLong = bToLong.applyAsLong(entry0.getKey());
                cApplier.accept(srcNode, Long.toString(bLong), entry0.getValue());
            }
        }
    }

    public <A, B, C> void getTriple(
            LongFunction<A> longToA,
            LongFunction<B> longToB,
            BiFunction<ObjectNode, String, C> cFunc,
            TripleMapping<A, B, C> out) {
        checkSimulationMode();
        ObjectNode obj = (ObjectNode) nextNode();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(obj)) {
            String aIdStr = entry.getKey();
            long aId = Long.parseLong(aIdStr);
            A a = longToA.apply(aId);

            ObjectNode child = (ObjectNode) entry.getValue();
            for(String bIdStr: Util.iterateFieldNames(child)) {
                long bId = Long.parseLong(bIdStr);
                B b = longToB.apply(bId);
                C c = cFunc.apply(child, bIdStr);
                out.put(a, b, c);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void putVarMap(
            VarCollection varColl,
            ToLongFunction<?>[] xToLong,
            TriConsumer<ObjectNode, String, ?> xApplier) {
        if(isSimulationMode()) return;
        ObjectNode obj = validateSet(nextPutId()).addObject();
        for(Object[] entry: varColl.iterable()) {
            ObjectNode node = obj;
            //total: 0..n-1
            //0..n-3
            for(int i = 0; i < entry.length - 2; i++) {
                Object value = entry[i];
                ToLongFunction toLongFunction = xToLong[i];
                long id = toLongFunction.applyAsLong(value);
                node = computeObjectIfAbsent(node, Long.toString(id));
            }
            //n-2 und n-1
            Object lastKey = entry[entry.length - 2];
            ToLongFunction lastToLong = xToLong[entry.length - 2];
            long lastId = lastToLong.applyAsLong(lastKey);
            Object lastValue = entry[entry.length - 1];
            ((TriConsumer) xApplier).accept(node, Long.toString(lastId), lastValue);
        }
    }

    public void getVarMap(
            LongFunction<?>[] longToX,
            BiFunction<ObjectNode, String, ?> xSupplier,
            VarCollection out) {
        checkSimulationMode();
        List<Object> temp = new ArrayList<>();
        ObjectNode obj = (ObjectNode) nextNode();
        recGetVarMap(obj, 0, longToX, xSupplier, temp, out);
        temp.clear();
    }

    protected void recGetVarMap(
            ObjectNode current,
            int i,
            LongFunction<?>[] longToX,
            BiFunction<ObjectNode, String, ?> xSupplier,
            List<Object> temp,
            VarCollection out) {
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(current)) {
            String idStr = entry.getKey();
            long id = Long.parseLong(idStr);
            Object idValue = longToX[i].apply(id);
            set(temp, i, idValue);

            JsonNode child = entry.getValue();
            if(child.isObject()) {
                ObjectNode objChild = (ObjectNode) child;
                recGetVarMap(objChild, i + 1, longToX, xSupplier, temp, out);
            } else {
                Object lastElement = xSupplier.apply(current, idStr);
                set(temp, i + 1, lastElement);
                out.add(temp);
            }
        }
    }

    private static void set(List<Object> list, int i, Object element) {
        while(list.size() < i + 1) {
            list.add(null);
        }
        list.set(i, element);
    }
}
