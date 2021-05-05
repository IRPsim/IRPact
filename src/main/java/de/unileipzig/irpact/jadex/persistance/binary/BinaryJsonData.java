package de.unileipzig.irpact.jadex.persistance.binary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;

/**
 * Stores data in a binary json (SMILE) format.
 *
 * @author Daniel Abitz
 */
public final class BinaryJsonData extends PersistableBase {

    public static ToLongFunction<Integer> INT2LONG = Number::longValue;
    public static LongFunction<Integer> LONG2INT = l -> (int) l;

    public static final int NOTHING_ID = -1;
    private static final int UID_ID = 0;
    private static final int TYPE_ID = 1;
    private static final int FIRST_AUTO_ID = 2;

    private static final String UID_ID_STR = "0";
    private static final String TYPE_ID_STR = "1";

    protected int autoPutId = FIRST_AUTO_ID;
    protected int autoGetId = FIRST_AUTO_ID;
    protected ObjectNode root;

    protected boolean simulationMode = false;
    protected boolean putMode = true;

    private BinaryJsonData(JsonNodeCreator creator) {
        this(creator.objectNode());
    }

    private BinaryJsonData(ObjectNode root) {
        this.root = root;
    }

    public static BinaryJsonData restore(byte[] data) throws IOException {
        ObjectNode node = (ObjectNode) IRPactJson.fromBytesWithSmile(data);
        BinaryJsonData bdata = new BinaryJsonData(node);
        long uid = bdata.ensureGetUid();
        bdata.setUID(uid);
        return bdata;
    }

    public static BinaryJsonData init(JsonNodeCreator creator, long uid, Class<?> c) {
        BinaryJsonData data = new BinaryJsonData(creator.objectNode());
        data.init(uid, c);
        return data;
    }

    public void setPutMode() {
        putMode = true;
    }

    public void setGetMode() {
        putMode = false;
    }

    private void init(long uid, Class<?> c) {
        if(isSimulationMode()) return;
        setUID(uid);
        root.put(UID_ID_STR, uid);
        root.put(TYPE_ID_STR, c.getName());
    }

    public long ensureGetUid() {
        checkSimulationMode();
        if(!root.has(UID_ID_STR)) {
            throw new NoSuchElementException("missing uid");
        }
        return root.get(UID_ID_STR).longValue();
    }

    public String ensureGetType() {
        checkSimulationMode();
        String type = root.get(TYPE_ID_STR).textValue();
        if(type == null || type.isEmpty()) {
            throw new NoSuchElementException("missing type");
        }
        return type;
    }

    private static String str(int id) {
        if(id == UID_ID || id == TYPE_ID) {
            throw new IllegalArgumentException("illegal id: " + id);
        }
        return Integer.toHexString(id);
    }

    public byte[] toBytes() throws IOException {
        checkSimulationMode();
        return IRPactJson.toBytesWithSmile(root);
    }

    public String printBytes() {
        try {
            return IRPactBase32.encodeToString(toBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    public ObjectNode getRoot() {
        return root;
    }

    public Collection<String> getFields() {
        List<String> list = new ArrayList<>();
        for(String str: Util.iterateFieldNames(root)) {
            list.add(str);
        }
        return list;
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

    private String nextPutId() {
        requiresPutMode();
        return str(autoPutId++);
    }

    public void putBoolean(boolean value) {
        if(isSimulationMode()) return;
        root.put(nextPutId(), value);
    }

    public void putInt(int value) {
        if(isSimulationMode()) return;
        root.put(nextPutId(), value);
    }

    public void putNothing() {
        if(isSimulationMode()) return;
        putLong(NOTHING_ID);
    }

    public void putLong(long value) {
        if(isSimulationMode()) return;
        root.put(nextPutId(), value);
    }

    public void putDouble(double value) {
        if(isSimulationMode()) return;
        root.put(nextPutId(), value);
    }

    public void putText(String value) {
        if(isSimulationMode()) return;
        root.put(nextPutId(), value);
    }

    public void putLongArray(long[] array) {
        if(isSimulationMode()) return;
        ArrayNode arr = root.putArray(nextPutId());
        for(long n: array) {
            arr.add(n);
        }
    }

    public void putLongMultiLongMap(Map<Long, List<Long>> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = root.putObject(nextPutId());
        for(Map.Entry<Long, List<Long>> entry: map.entrySet()) {
            ArrayNode arr = obj.putArray(Long.toString(entry.getKey()));
            for(long n: entry.getValue()) {
                arr.add(n);
            }
        }
    }

    public void putLongMultiStringMap(Map<Long, List<String>> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = root.putObject(nextPutId());
        for(Map.Entry<Long, List<String>> entry: map.entrySet()) {
            ArrayNode arr = obj.putArray(Long.toString(entry.getKey()));
            for(String n: entry.getValue()) {
                arr.add(n);
            }
        }
    }

    public void putLongLongMap(Map<Long, Long> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = root.putObject(nextPutId());
        for(Map.Entry<Long, Long> entry: map.entrySet()) {
            obj.put(Long.toString(entry.getKey()), entry.getValue());
        }
    }

    public void putLongDoubleMap(Map<Long, Double> map) {
        if(isSimulationMode()) return;
        ObjectNode obj = root.putObject(nextPutId());
        for(Map.Entry<Long, Double> entry: map.entrySet()) {
            obj.put(Long.toString(entry.getKey()), entry.getValue());
        }
    }

    public void putLongLongDoubleTable(Map<Long, Map<Long, Double>> table) {
        if(isSimulationMode()) return;
        ObjectNode obj = root.putObject(nextPutId());
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
            throw new IllegalStateException("get mode");
        }
    }

    public void resetGet() {
        autoGetId = FIRST_AUTO_ID;
    }

    private String nextGetId() {
        requiresGetMode();
        return str(autoGetId++);
    }

    private JsonNode nextNode() {
        return nextNode(false);
    }

    private JsonNode nextNodeAllowNull() {
        return nextNode(true);
    }

    private JsonNode nextNode(boolean allowNull) {
        String id = nextGetId();
        JsonNode node = root.get(id);
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
    //util
    //=========================

    public static <K> Map<Long, Double> mapToLongDoubleMap(
            Map<K, ? extends Number> input,
            PersistManager manager) {
        return mapToLongDoubleMap(
                input,
                manager::ensureGetUID,
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
                manager::ensureGetUID,
                manager::ensureGetUID
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
                manager::ensureGet,
                manager::ensureGet,
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
}
