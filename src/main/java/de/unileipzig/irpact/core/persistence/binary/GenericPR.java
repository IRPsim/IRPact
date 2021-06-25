package de.unileipzig.irpact.core.persistence.binary;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.annotation.*;
import de.unileipzig.irpact.jadex.persistance.binary.data.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class GenericPR<T> extends BinaryPRBase<T> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GenericPR.class);

    private final Class<T> c;

    protected final List<PrimitiveMultiEntry> primitiveEntries = new ArrayList<>();
    protected final List<NonPrimitiveMultiEntry> nonPrimitiveEntries = new ArrayList<>();
    protected final List<CollectionMultiEntry> collectionMultiEntries = new ArrayList<>();
    protected final List<MapMultiEntry> mapMultiEntries = new ArrayList<>();
    protected final List<MapMapMultiEntry> mapMapMultiEntries = new ArrayList<>();

    public GenericPR(Class<T> c) throws IRPactException {
        this.c = c;
        validate(c);
        initalize();
    }

    @Override
    public Class<T> getType() {
        return c;
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    public List<PrimitiveMultiEntry> getPrimitiveEntries() {
        return primitiveEntries;
    }

    public List<NonPrimitiveMultiEntry> getNonPrimitiveEntries() {
        return nonPrimitiveEntries;
    }

    public List<CollectionMultiEntry> getCollectionMultiEntries() {
        return collectionMultiEntries;
    }

    public List<MapMultiEntry> getMapMultiEntries() {
        return mapMultiEntries;
    }

    public List<MapMapMultiEntry> getMapMapMultiEntries() {
        return mapMapMultiEntries;
    }

    //==================================================
    //TaskManager
    //==================================================

    protected static final TaskManager TASKS = createTasks();
    
    public static TaskManager getTasks() {
        return TASKS;
    }

    protected static TaskManager createTasks() {
        TaskManager m = new TaskManager();

        //PutPrimitiveIntoData
        m.registerPutPrimitiveIntoData(boolean.class, (data, getter, ref) -> data.putBoolean((boolean) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(Boolean.class, (data, getter, ref) -> data.putBoolean((Boolean) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(int.class, (data, getter, ref) -> data.putInt((int) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(Integer.class, (data, getter, ref) -> data.putInt((Integer) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(long.class, (data, getter, ref) -> data.putLong((long) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(Long.class, (data, getter, ref) -> data.putLong((Long) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(double.class, (data, getter, ref) -> data.putDouble((double) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(Double.class, (data, getter, ref) -> data.putDouble((Long) getter.invoke(ref)));
        m.registerPutPrimitiveIntoData(String.class, (data, getter, ref) -> data.putText((String) getter.invoke(ref)));

        //GetPrimitiveFromData
        m.registerGetPrimitiveFromData(boolean.class, (data, setter, ref) -> setter.invoke(ref, data.getBoolean()));
        m.registerGetPrimitiveFromData(Boolean.class, (data, setter, ref) -> setter.invoke(ref, data.getBoolean()));
        m.registerGetPrimitiveFromData(int.class, (data, setter, ref) -> setter.invoke(ref, data.getInt()));
        m.registerGetPrimitiveFromData(Integer.class, (data, setter, ref) -> setter.invoke(ref, data.getInt()));
        m.registerGetPrimitiveFromData(long.class, (data, setter, ref) -> setter.invoke(ref, data.getLong()));
        m.registerGetPrimitiveFromData(Long.class, (data, setter, ref) -> setter.invoke(ref, data.getLong()));
        m.registerGetPrimitiveFromData(Double.class, (data, setter, ref) -> setter.invoke(ref, data.getDouble()));
        m.registerGetPrimitiveFromData(Double.class, (data, setter, ref) -> setter.invoke(ref, data.getDouble()));
        m.registerGetPrimitiveFromData(String.class, (data, setter, ref) -> setter.invoke(ref, data.getText()));

        //ToStringKey
        m.registerToStringKey(MappingMode.ID, (manager, key) -> Long.toString(manager.uncheckedEnsureGetUID(key)));
        m.registerToStringKey(MappingMode.BOOLEAN, (manager, key) -> Boolean.toString((Boolean) key));
        m.registerToStringKey(MappingMode.INT, (manager, key) -> Integer.toString((Integer) key));
        m.registerToStringKey(MappingMode.LONG, (manager, key) -> Long.toString((Long) key));
        m.registerToStringKey(MappingMode.DOUBLE, (manager, key) -> Double.toString((Double) key));
        m.registerToStringKey(MappingMode.STRING, (manager, key) -> (String) key);

        //ToStringKey
        m.registerFromStringKey(MappingMode.ID, (manager, key) -> manager.uncheckedEnsureGet(Long.parseLong(key)));
        m.registerFromStringKey(MappingMode.BOOLEAN, (manager, key) -> Boolean.parseBoolean(key));
        m.registerFromStringKey(MappingMode.INT, (manager, key) -> Integer.parseInt(key));
        m.registerFromStringKey(MappingMode.LONG, (manager, key) -> Long.parseLong(key));
        m.registerFromStringKey(MappingMode.DOUBLE, (manager, key) -> Double.parseDouble(key));
        m.registerFromStringKey(MappingMode.STRING, (manager, key) -> key);

        //AddPrimitiveToArrayNode
        m.registerAddToArrayNode(MappingMode.ID, (manager, arr, value) -> arr.add(manager.uncheckedEnsureGetUID(value)));
        m.registerAddToArrayNode(MappingMode.BOOLEAN, (manager, arr, value) -> arr.add((Boolean) value));
        m.registerAddToArrayNode(MappingMode.INT, (manager, arr, value) -> arr.add((Integer) value));
        m.registerAddToArrayNode(MappingMode.LONG, (manager, arr, value) -> arr.add((Long) value));
        m.registerAddToArrayNode(MappingMode.DOUBLE, (manager, arr, value) -> arr.add((Double) value));
        m.registerAddToArrayNode(MappingMode.STRING, (manager, arr, value) -> arr.add((String) value));

        //GetFromArrayNode
        m.registerGetFromArrayNode(MappingMode.ID, (manager, arr, index) -> manager.uncheckedEnsureGet(arr.get(index).longValue()));
        m.registerGetFromArrayNode(MappingMode.BOOLEAN, (manager, arr, index) -> arr.get(index).booleanValue());
        m.registerGetFromArrayNode(MappingMode.INT, (manager, arr, index) -> arr.get(index).booleanValue());
        m.registerGetFromArrayNode(MappingMode.LONG, (manager, arr, index) -> arr.get(index).longValue());
        m.registerGetFromArrayNode(MappingMode.DOUBLE, (manager, arr, index) -> arr.get(index).doubleValue());
        m.registerGetFromArrayNode(MappingMode.STRING, (manager, arr, index) -> arr.get(index).textValue());

        //PutPrimitiveIntoObjectNode
        m.registerPutIntoObjectNode(MappingMode.ID, (manager, obj, key, value) -> obj.put(key, manager.uncheckedEnsureGetUID(value)));
        m.registerPutIntoObjectNode(MappingMode.BOOLEAN, (manager, obj, key, value) -> obj.put(key, (Boolean) value));
        m.registerPutIntoObjectNode(MappingMode.INT, (manager, obj, key, value) -> obj.put(key, (Integer) value));
        m.registerPutIntoObjectNode(MappingMode.LONG, (manager, obj, key, value) -> obj.put(key, (Long) value));
        m.registerPutIntoObjectNode(MappingMode.DOUBLE, (manager, obj, key, value) -> obj.put(key, (Double) value));
        m.registerPutIntoObjectNode(MappingMode.STRING, (manager, obj, key, value) -> obj.put(key, (String) value));

        //GetFromObjectNode
        m.registerGetFromObjectNode(MappingMode.ID, (manager, obj, key) -> manager.uncheckedEnsureGet(obj.get(key).longValue()));
        m.registerGetFromObjectNode(MappingMode.BOOLEAN, (manager, obj, key) -> obj.get(key).booleanValue());
        m.registerGetFromObjectNode(MappingMode.INT, (manager, obj, key) -> obj.get(key).intValue());
        m.registerGetFromObjectNode(MappingMode.LONG, (manager, obj, key) -> obj.get(key).longValue());
        m.registerGetFromObjectNode(MappingMode.DOUBLE, (manager, obj, key) -> obj.get(key).doubleValue());
        m.registerGetFromObjectNode(MappingMode.STRING, (manager, obj, key) -> obj.get(key).textValue());

        return m;
    }

    /**
     * @author Daniel Abitz
     */
    public static class TaskManager {

        protected Map<Class<?>, PutPrimitiveIntoData> putPrimitiveIntoDataMap;
        protected Map<Class<?>, GetPrimitiveFromData> getPrimitiveFromDataMap;
        protected Map<MappingMode, ToStringKey> toStringKeyMap;
        protected Map<MappingMode, FromStringKey> fromStringKeyMap;
        protected Map<MappingMode, AddToArrayNode> addToArrayNodeMap;
        protected Map<MappingMode, GetFromArrayNode> getFromArrayNodeMap;
        protected Map<MappingMode, PutIntoObjectNode> putIntoObjectNodeMap;
        protected Map<MappingMode, GetFromObjectNode> getFromObjectNodeMap;

        public TaskManager() {
            this(
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>(),
                    new HashMap<>()
            );
        }

        public TaskManager(
                Map<Class<?>, PutPrimitiveIntoData> putPrimitiveIntoDataMap,
                Map<Class<?>, GetPrimitiveFromData> getPrimitiveFromDataMap,
                Map<MappingMode, ToStringKey> toStringKeyMap,
                Map<MappingMode, FromStringKey> fromStringKeyMap,
                Map<MappingMode, AddToArrayNode> addToArrayNodeMap,
                Map<MappingMode, GetFromArrayNode> getFromArrayNodeMap,
                Map<MappingMode, PutIntoObjectNode> putIntoObjectNodeMap,
                Map<MappingMode, GetFromObjectNode> getFromObjectNodeMap) {
            this.putPrimitiveIntoDataMap = putPrimitiveIntoDataMap;
            this.getPrimitiveFromDataMap = getPrimitiveFromDataMap;
            this.toStringKeyMap = toStringKeyMap;
            this.fromStringKeyMap = fromStringKeyMap;
            this.addToArrayNodeMap = addToArrayNodeMap;
            this.getFromArrayNodeMap = getFromArrayNodeMap;
            this.putIntoObjectNodeMap = putIntoObjectNodeMap;
            this.getFromObjectNodeMap = getFromObjectNodeMap;
        }

        public void registerPutPrimitiveIntoData(Class<?> c, PutPrimitiveIntoData task) {
            putPrimitiveIntoDataMap.put(c, task);
        }
        public PutPrimitiveIntoData getPutPrimitiveIntoData(Class<?> c) {
            return putPrimitiveIntoDataMap.get(c);
        }

        public void registerGetPrimitiveFromData(Class<?> c, GetPrimitiveFromData task) {
            getPrimitiveFromDataMap.put(c, task);
        }
        public GetPrimitiveFromData getGetPrimitiveFromData(Class<?> c) {
            return getPrimitiveFromDataMap.get(c);
        }

        public void registerToStringKey(MappingMode c, ToStringKey task) {
            toStringKeyMap.put(c, task);
        }
        public ToStringKey getToStringKey(MappingMode c) {
            return toStringKeyMap.get(c);
        }

        public void registerFromStringKey(MappingMode c, FromStringKey task) {
            fromStringKeyMap.put(c, task);
        }
        public FromStringKey getFromStringKey(MappingMode c) {
            return fromStringKeyMap.get(c);
        }

        public void registerAddToArrayNode(MappingMode c, AddToArrayNode task) {
            addToArrayNodeMap.put(c, task);
        }
        public AddToArrayNode getAddToArrayNode(MappingMode c) {
            return addToArrayNodeMap.get(c);
        }

        public void registerGetFromArrayNode(MappingMode c, GetFromArrayNode task) {
            getFromArrayNodeMap.put(c, task);
        }
        public GetFromArrayNode getGetFromArrayNode(MappingMode c) {
            return getFromArrayNodeMap.get(c);
        }

        public void registerPutIntoObjectNode(MappingMode c, PutIntoObjectNode task) {
            putIntoObjectNodeMap.put(c, task);
        }
        public PutIntoObjectNode getPutIntoObjectNode(MappingMode c) {
            return putIntoObjectNodeMap.get(c);
        }

        public void registerGetFromObjectNode(MappingMode c, GetFromObjectNode task) {
            getFromObjectNodeMap.put(c, task);
        }
        public GetFromObjectNode getGetFromObjectNode(MappingMode c) {
            return getFromObjectNodeMap.get(c);
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface PutPrimitiveIntoData {

        void handle(BinaryJsonData data, Method getter, Object ref) throws InvocationTargetException, IllegalAccessException;
    }

    /**
     * @author Daniel Abitz
     */
    public interface GetPrimitiveFromData {

        void handle(BinaryJsonData data, Method setter, Object ref) throws InvocationTargetException, IllegalAccessException;
    }

    /**
     * @author Daniel Abitz
     */
    public interface ToStringKey {

        String handle(PersistManager manager, Object key);
    }

    /**
     * @author Daniel Abitz
     */
    public interface FromStringKey {

        Object handle(RestoreManager manager, String key);
    }

    /**
     * @author Daniel Abitz
     */
    public interface AddToArrayNode {

        void handle(PersistManager manager, ArrayNode arr, Object value);
    }

    /**
     * @author Daniel Abitz
     */
    public interface GetFromArrayNode {

        Object handle(RestoreManager manager, ArrayNode arr, int index);
    }

    /**
     * @author Daniel Abitz
     */
    public interface PutIntoObjectNode {

        void handle(PersistManager manager, ObjectNode obj, String key, Object value);
    }

    /**
     * @author Daniel Abitz
     */
    public interface GetFromObjectNode {

        Object handle(RestoreManager manager, ObjectNode obj, String key);
    }

    //==================================================
    //validate
    //==================================================

    public static final String IGNORE = "__IGNORE__";

    public static final Set<Class<?>> PRIMITIVES = Collections.unmodifiableSet(CollectionUtil.hashSetOf(
            String.class,

            byte.class, Byte.class,
            short.class, Short.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class,
            boolean.class, Boolean.class,
            char.class, Character.class
    ));

    //=========================
    //general
    //=========================

    public static boolean isPrimitive(Field field) {
        return isPrimitive(field.getDeclaringClass());
    }

    public static boolean isPrimitive(Class<?> c) {
        return PRIMITIVES.contains(c);
    }

    public static boolean isNotPrimitive(Field field) {
        return isNotPrimitive(field.getDeclaringClass());
    }

    public static boolean isNotPrimitive(Class<?> c) {
        return !PRIMITIVES.contains(c);
    }

    public static boolean isValid(String input) {
        return input != null && !input.isEmpty() && !IGNORE.equals(input);
    }

    public static boolean isIgnoreOrEquals(String input, String other) {
        return IGNORE.equals(input) || Objects.equals(input, other);
    }

    public static String getGetterName(Field field) {
        return "get" + StringUtil.firstLetterToUpperCase(field.getName());
    }

    public static String getSetterName(Field field) {
        return "set" + StringUtil.firstLetterToUpperCase(field.getName());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasSetMethod(Class<?> c, String methodName, Class<?> param) {
        try {
            c.getMethod(methodName, param);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasGetMethod(Class<?> c, String methodName, Class<?> returnType) {
        try {
            Method m = c.getMethod(methodName);
            return m.getReturnType() == returnType;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    //=========================
    //general
    //=========================

    public static boolean persistWith(Class<?> c, PersistManager manager) {
        return handleWith(c, manager, null);
    }

    public static boolean restoreWith(Class<?> c, RestoreManager manager) {
        return handleWith(c, null, manager);
    }

    private static boolean handleWith(Class<?> c, PersistManager persistManager, RestoreManager restoreManager) {
        if(c.isAnnotationPresent(UseBinaryPersisters.class)) {
            UseBinaryPersisters annotations = c.getDeclaredAnnotation(UseBinaryPersisters.class);
            for(UseBinaryPersist annotation: annotations.value()) {
                if(persistManager == null) {
                    if(isIgnoreOrEquals(annotation.restorerName(), restoreManager.getName())) {
                        return true;
                    }
                } else {
                    if(isIgnoreOrEquals(annotation.persisterName(), persistManager.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void validate(Class<?> c) throws IRPactException {
        for(Field field: c.getFields()) {
            if(field.isAnnotationPresent(PrimitiveBinaryPersisters.class)) {
                PrimitiveBinaryPersisters annotation = field.getDeclaredAnnotation(PrimitiveBinaryPersisters.class);
                validate(annotation, c, field);
            }
            else if(field.isAnnotationPresent(NonPrimitiveBinaryPersisters.class)) {
                NonPrimitiveBinaryPersisters annotation = field.getDeclaredAnnotation(NonPrimitiveBinaryPersisters.class);
                validate(annotation, c, field);
            }
            else if(field.isAnnotationPresent(CollectionBinaryPersisters.class)) {
                CollectionBinaryPersisters annotation = field.getDeclaredAnnotation(CollectionBinaryPersisters.class);
                validate(annotation, c, field);
            }
            else if(field.isAnnotationPresent(MapBinaryPersisters.class)) {
                MapBinaryPersisters annotation = field.getDeclaredAnnotation(MapBinaryPersisters.class);
                validate(annotation, c, field);
            }
        }
    }

    //=========================
    //PrimitiveBinaryPersist
    //=========================

    public static void validate(PrimitiveBinaryPersisters annotations, Class<?> c, Field annotatedField) throws IRPactException {
        for(PrimitiveBinaryPersist annotation: annotations.value()) {
            validate(annotation, c, annotatedField);
        }
    }

    public static void validate(PrimitiveBinaryPersist annotation, Class<?> c, Field annotatedField) throws IRPactException {
        if(isNotPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is not primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getType().getName());
        }

        String setter = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        if(!hasSetMethod(c, setter, annotatedField.getType())) {
            throw new IRPactException("setter '{}' not found for {}#{}", setter, c.getName(), annotatedField.getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getType())) {
            throw new IRPactException("getter '{}' not found for {}#{}", getter, c.getName(), annotatedField.getName());
        }
    }

    //=========================
    //NonPrimitiveBinaryPersisters
    //=========================

    public static void validate(NonPrimitiveBinaryPersisters annotations, Class<?> c, Field annotatedField) throws IRPactException {
        for(NonPrimitiveBinaryPersist annotation: annotations.value()) {
            validate(annotation, c, annotatedField);
        }
    }

    public static void validate(NonPrimitiveBinaryPersist annotation, Class<?> c, Field annotatedField) throws IRPactException {
        if(isPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getType().getName());
        }

        String setter = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        if(!hasSetMethod(c, setter, annotatedField.getType())) {
            throw new IRPactException("setter '{}' not found for {}#{}", setter, c.getName(), annotatedField.getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getType())) {
            throw new IRPactException("getter '{}' not found for {}#{}", getter, c.getName(), annotatedField.getName());
        }
    }

    //=========================
    //CollectionBinaryPersisters
    //=========================

    public static void validate(CollectionBinaryPersisters annotations, Class<?> c, Field annotatedField) throws IRPactException {
        for(CollectionBinaryPersist annotation: annotations.value()) {
            validate(annotation, c, annotatedField);
        }
    }

    public static void validate(CollectionBinaryPersist annotation, Class<?> c, Field annotatedField) throws IRPactException {
        if(isPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getType().getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getType())) {
            throw new IRPactException("getter '{}' not found for {}#{}", getter, c.getName(), annotatedField.getName());
        }
    }

    //=========================
    //MapBinaryPersisters
    //=========================

    public static void validate(MapBinaryPersisters annotations, Class<?> c, Field annotatedField) throws IRPactException {
        for(MapBinaryPersist annotation: annotations.value()) {
            validate(annotation, c, annotatedField);
        }
    }

    public static void validate(MapBinaryPersist annotation, Class<?> c, Field annotatedField) throws IRPactException {
        if(isPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getType().getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getType())) {
            throw new IRPactException("getter '{}' not found for {}#{}", getter, c.getName(), annotatedField.getName());
        }
    }

    //=========================
    //MapMapBinaryPersisters
    //=========================

    public static void validate(MapMapBinaryPersisters annotations, Class<?> c, Field annotatedField) throws IRPactException {
        for(MapMapBinaryPersist annotation: annotations.value()) {
            validate(annotation, c, annotatedField);
        }
    }

    public static void validate(MapMapBinaryPersist annotation, Class<?> c, Field annotatedField) throws IRPactException {
        if(isPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getType().getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getType())) {
            throw new IRPactException("getter '{}' not found for {}#{}", getter, c.getName(), annotatedField.getName());
        }
    }

    //==================================================
    //init
    //==================================================

    protected void initalize() throws IRPactException {
        try {
            initalizeFields();
        } catch (NoSuchMethodException e) {
            throw new IRPactException(e);
        }
    }

    protected void initalizeFields() throws NoSuchMethodException {
        for(Field field: getType().getDeclaredFields()) {
            if(field.isAnnotationPresent(PrimitiveBinaryPersisters.class)) {
                System.out.println("XXX");
                PrimitiveBinaryPersisters annotation = field.getDeclaredAnnotation(PrimitiveBinaryPersisters.class);
                PrimitiveMultiEntry multiEntry = handle(annotation, field);
                primitiveEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(PrimitiveBinaryPersist.class)) {
                System.out.println("YYY");
                PrimitiveBinaryPersist annotation = field.getDeclaredAnnotation(PrimitiveBinaryPersist.class);
                PrimitiveEntry entry = handle(annotation, field);
                PrimitiveMultiEntryImpl multiEntry = new PrimitiveMultiEntryImpl();
                multiEntry.add(entry);
                primitiveEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(NonPrimitiveBinaryPersisters.class)) {
                NonPrimitiveBinaryPersisters annotation = field.getDeclaredAnnotation(NonPrimitiveBinaryPersisters.class);
                NonPrimitiveMultiEntry multiEntry = handle(annotation, field);
                nonPrimitiveEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(NonPrimitiveBinaryPersist.class)) {
                NonPrimitiveBinaryPersist annotation = field.getDeclaredAnnotation(NonPrimitiveBinaryPersist.class);
                NonPrimitiveEntry entry = handle(annotation, field);
                NonPrimitiveMultiEntryImpl multiEntry = new NonPrimitiveMultiEntryImpl();
                multiEntry.add(entry);
                nonPrimitiveEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(CollectionBinaryPersisters.class)) {
                CollectionBinaryPersisters annotation = field.getDeclaredAnnotation(CollectionBinaryPersisters.class);
                CollectionMultiEntry multiEntry = handle(annotation, field);
                collectionMultiEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(CollectionBinaryPersist.class)) {
                CollectionBinaryPersist annotation = field.getDeclaredAnnotation(CollectionBinaryPersist.class);
                CollectionEntry entry = handle(annotation, field);
                CollectionMultiEntryImpl multiEntry = new CollectionMultiEntryImpl();
                multiEntry.add(entry);
                collectionMultiEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(MapBinaryPersisters.class)) {
                MapBinaryPersisters annotation = field.getDeclaredAnnotation(MapBinaryPersisters.class);
                MapMultiEntry multiEntry = handle(annotation, field);
                mapMultiEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(MapBinaryPersist.class)) {
                MapBinaryPersist annotation = field.getDeclaredAnnotation(MapBinaryPersist.class);
                MapEntry entry = handle(annotation, field);
                MapMultiEntryImpl multiEntry = new MapMultiEntryImpl();
                multiEntry.add(entry);
                mapMultiEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(MapMapBinaryPersisters.class)) {
                MapMapBinaryPersisters annotation = field.getDeclaredAnnotation(MapMapBinaryPersisters.class);
                MapMapMultiEntry multiEntry = handle(annotation, field);
                mapMapMultiEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(MapMapBinaryPersist.class)) {
                MapMapBinaryPersist annotation = field.getDeclaredAnnotation(MapMapBinaryPersist.class);
                MapMapEntry entry = handle(annotation, field);
                MapMapMultiEntryImpl multiEntry = new MapMapMultiEntryImpl();
                multiEntry.add(entry);
                mapMapMultiEntries.add(multiEntry);
            }
        }
    }

    protected PrimitiveMultiEntry handle(PrimitiveBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        PrimitiveMultiEntryImpl multiEntry = new PrimitiveMultiEntryImpl();
        for(PrimitiveBinaryPersist annotation: annotions.value()) {
            PrimitiveEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    protected PrimitiveEntry handle(PrimitiveBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        PrimitiveEntryImpl entry = new PrimitiveEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        String setterName = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        Method setter = getType().getDeclaredMethod(setterName, annotatedField.getType());
        entry.setSetter(setter);

        return entry;
    }

    protected NonPrimitiveMultiEntry handle(NonPrimitiveBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        NonPrimitiveMultiEntryImpl multiEntry = new NonPrimitiveMultiEntryImpl();
        for(NonPrimitiveBinaryPersist annotation: annotions.value()) {
            NonPrimitiveEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    protected NonPrimitiveEntry handle(NonPrimitiveBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        NonPrimitiveEntryImpl entry = new NonPrimitiveEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        String setterName = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        Method setter = getType().getDeclaredMethod(setterName, annotatedField.getType());
        entry.setSetter(setter);

        return entry;
    }

    protected CollectionMultiEntry handle(CollectionBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        CollectionMultiEntryImpl multiEntry = new CollectionMultiEntryImpl();
        for(CollectionBinaryPersist annotation: annotions.value()) {
            CollectionEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    protected CollectionEntry handle(CollectionBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        CollectionEntryImpl entry = new CollectionEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        entry.setSetter(null);
        entry.setMode(annotation.mode());

        return entry;
    }

    protected MapMultiEntry handle(MapBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        MapMultiEntryImpl multiEntry = new MapMultiEntryImpl();
        for(MapBinaryPersist annotation: annotions.value()) {
            MapEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    protected MapEntry handle(MapBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        MapEntryImpl entry = new MapEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        entry.setSetter(null);
        entry.setKeyMode(annotation.keyMode());
        entry.setValueMode(annotation.valueMode());

        return entry;
    }

    protected MapMapMultiEntry handle(MapMapBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        MapMapMultiEntryImpl multiEntry = new MapMapMultiEntryImpl();
        for(MapMapBinaryPersist annotation: annotions.value()) {
            MapMapEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    protected MapMapEntry handle(MapMapBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        MapMapEntryImpl entry = new MapMapEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        entry.setSetter(null);
        entry.setFirstMode(annotation.firstMode());
        entry.setSecondMode(annotation.secondMode());
        entry.setThirdMode(annotation.thirdMode());
        entry.setSupplier(annotation.supplier());

        return entry;
    }

    //==================================================
    //persist util
    //==================================================

    protected static void putPrimitive(BinaryJsonData data, Method getter, Object ref) throws InvocationTargetException, IllegalAccessException {
        Class<?> type = getter.getReturnType();
        PutPrimitiveIntoData task = getTasks().getPutPrimitiveIntoData(type);
        if(task == null) {
            throw new IRPactIllegalArgumentException("unsupported return type: {}", getter.getReturnType());
        } else {
            task.handle(data, getter, ref);
        }
    }

    protected static void prepareNonPrimitive(PersistManager manager, Method getter, Object ref) throws InvocationTargetException, IllegalAccessException, PersistException {
        Object result = getter.invoke(ref);
        manager.prepare(result);
    }

    protected static void putNonPrimitive(PersistManager manager, BinaryJsonData data, Method getter, Object ref) throws PersistException, InvocationTargetException, IllegalAccessException {
        Object result = getter.invoke(ref);
        long id = manager.ensureGetUID(result);
        data.putLong(id);
    }

    protected static void prepareCollection(PersistManager manager, Method getter, Object ref, MappingMode mode) throws InvocationTargetException, IllegalAccessException, PersistException {
        if(mode == MappingMode.ID) {
            Collection<?> coll = (Collection<?>) getter.invoke(ref);
            for(Object entry: coll) {
                manager.prepare(entry);
            }
        }
    }

    protected static void putCollection(PersistManager manager, BinaryJsonData data, Method getter, Object ref, MappingMode mode) throws InvocationTargetException, IllegalAccessException {
        Collection<?> coll = (Collection<?>) getter.invoke(ref);
        AddToArrayNode task = getTasks().getAddToArrayNode(mode);
        if(task == null) {
            throw new IllegalArgumentException("unsupported mode: " + mode);
        } else {
            data.putCollection(
                    coll,
                    (arr, obj) -> task.handle(manager, arr, obj)
            );
        }
    }

    protected static void prepareMap(PersistManager manager, Method getter, Object ref, MappingMode keyMode, MappingMode valueMode) throws InvocationTargetException, IllegalAccessException, PersistException {
        Map<?,?> map = (Map<?,?>) getter.invoke(ref);
        for(Map.Entry<?, ?> entry: map.entrySet()) {
            if(keyMode == MappingMode.ID) {
                manager.prepare(entry.getKey());
            }

            if(valueMode == MappingMode.ID) {
                manager.prepare(entry.getValue());
            }
        }
    }

    protected static void putMap(PersistManager manager, BinaryJsonData data, Method getter, Object ref, MappingMode keyMode, MappingMode valueMode) throws InvocationTargetException, IllegalAccessException {
        Map<?, ?> map = (Map<?, ?>) getter.invoke(ref);
        ToStringKey keyTask = getTasks().getToStringKey(keyMode);
        if(keyTask == null) {
            throw new IllegalArgumentException("unsupported key mode: " + keyMode);
        }
        PutIntoObjectNode valueTask = getTasks().getPutIntoObjectNode(valueMode);
        if(valueTask == null) {
            throw new IllegalArgumentException("unsupported value mode: " + valueMode);
        }

        data.putMap(
                map,
                key -> keyTask.handle(manager, key),
                (obj, key, val) -> valueTask.handle(manager, obj, key, val)
        );
    }

    @SuppressWarnings("unchecked")
    protected static void prepareMapMap(PersistManager manager, Method getter, Object ref, MappingMode firstMode, MappingMode secondMode, MappingMode thirdMode) throws InvocationTargetException, IllegalAccessException, PersistException {
        Map<?, Map<?, ?>> map = (Map<?, Map<?, ?>>) getter.invoke(ref);
        for(Map.Entry<?, Map<?, ?>> entry: map.entrySet()) {
            if(firstMode == MappingMode.ID) {
                manager.prepare(entry.getKey());
            }

            for(Map.Entry<?, ?> entry1: entry.getValue().entrySet()) {
                if(secondMode == MappingMode.ID) {
                    manager.prepare(entry1.getKey());
                }

                if(thirdMode == MappingMode.ID) {
                    manager.prepare(entry1.getValue());
                }
            }
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void putMapMap(PersistManager manager, BinaryJsonData data, Method getter, Object ref, MappingMode firstMode, MappingMode secondMode, MappingMode thirdMode) throws InvocationTargetException, IllegalAccessException {
        Map map = (Map) getter.invoke(ref);
        ToStringKey firstKey = getTasks().getToStringKey(firstMode);
        if(firstKey == null) {
            throw new IllegalArgumentException("unsupported first mode: " + firstMode);
        }
        ToStringKey secondKey = getTasks().getToStringKey(secondMode);
        if(secondKey == null) {
            throw new IllegalArgumentException("unsupported second mode: " + secondMode);
        }
        PutIntoObjectNode thirdTask = getTasks().getPutIntoObjectNode(thirdMode);
        if(thirdTask == null) {
            throw new IllegalArgumentException("unsupported third mode: " + thirdMode);
        }

        data.putMapMap(
                map,
                key1 -> firstKey.handle(manager, key1),
                key2 -> secondKey.handle(manager, key2),
                (obj, key, val) -> thirdTask.handle(manager, obj, key, val)
        );
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(T object, PersistManager manager) throws PersistException {
        try {
            BinaryJsonData data = initData(object, manager);

            for(PrimitiveMultiEntry multiEntry: primitiveEntries) {
                PrimitiveEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    putPrimitive(data, entry.getter(), object);
                }
            }

            for(NonPrimitiveMultiEntry multiEntry: nonPrimitiveEntries) {
                NonPrimitiveEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    prepareNonPrimitive(manager, entry.getter(), object);
                }
            }

            for(CollectionMultiEntry multiEntry: collectionMultiEntries) {
                CollectionEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    prepareCollection(manager, entry.getter(), object, entry.mode());
                }
            }

            for(MapMultiEntry multiEntry: mapMultiEntries) {
                MapEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    prepareMap(manager, entry.getter(), object, entry.keyMode(), entry.valueMode());
                }
            }

            for(MapMapMultiEntry multiEntry: mapMapMultiEntries) {
                MapMapEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    prepareMapMap(manager, entry.getter(), object, entry.firstMode(), entry.secondMode(), entry.thirdMode());
                }
            }

            return data;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void doSetupPersist(T object, BinaryJsonData data, PersistManager manager) throws PersistException {
        try {
            for(NonPrimitiveMultiEntry multiEntry: nonPrimitiveEntries) {
                NonPrimitiveEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    putNonPrimitive(manager, data, entry.getter(), object);
                }
            }

            for(CollectionMultiEntry multiEntry: collectionMultiEntries) {
                CollectionEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    putCollection(manager, data, entry.getter(), object, entry.mode());
                }
            }

            for(MapMultiEntry multiEntry: mapMultiEntries) {
                MapEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    putMap(manager, data, entry.getter(), object, entry.keyMode(), entry.valueMode());
                }
            }

            for(MapMapMultiEntry multiEntry: mapMapMultiEntries) {
                MapMapEntry entry = multiEntry.getForPersist(manager.getName());
                if(entry != null) {
                    putMapMap(manager, data, entry.getter(), object, entry.firstMode(), entry.secondMode(), entry.thirdMode());
                }
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new PersistException(e);
        }
    }

    //==================================================
    //restore util
    //==================================================

    protected T createInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return getType().getDeclaredConstructor()
                .newInstance();
    }

    protected static Class<?> getParameterType(Method setter) {
        return setter.getParameterTypes()[0];
    }

    protected static void getPrimitive(BinaryJsonData data, Method setter, Object ref) throws InvocationTargetException, IllegalAccessException {
        Class<?> type = getParameterType(setter);
        GetPrimitiveFromData task = getTasks().getGetPrimitiveFromData(type);
        if(task == null) {
            throw new IRPactIllegalArgumentException("unsupported return type: {}", setter.getReturnType());
        } else {
            task.handle(data, setter, ref);
        }
    }

    protected static void getNonPrimitive(RestoreManager manager, BinaryJsonData data, Method setter, Object ref) throws InvocationTargetException, IllegalAccessException, RestoreException {
        long id = data.getLong();
        Object obj = manager.ensureGet(id);
        setter.invoke(ref, obj);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void getCollection(RestoreManager manager, BinaryJsonData data, Method getter, Object ref, MappingMode mode) throws InvocationTargetException, IllegalAccessException {
        Collection coll = (Collection) getter.invoke(ref);
        GetFromArrayNode task = getTasks().getGetFromArrayNode(mode);
        if(task == null) {
            throw new IllegalArgumentException("unsupported mode: " + mode);
        }

        data.getCollection(
                (arr, index) -> task.handle(manager, arr, index),
                coll
        );
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static void getMap(RestoreManager manager, BinaryJsonData data, Method getter, Object ref, MappingMode keyMode, MappingMode valueMode) throws InvocationTargetException, IllegalAccessException {
        Map map = (Map) getter.invoke(ref);
        FromStringKey keyTask = getTasks().getFromStringKey(keyMode);
        if(keyTask == null) {
            throw new IllegalArgumentException("unsupported key mode: " + keyMode);
        }
        GetFromObjectNode valueTask = getTasks().getGetFromObjectNode(valueMode);
        if(valueTask == null) {
            throw new IllegalArgumentException("unsupported key mode: " + valueMode);
        }

        data.getMap(
                key -> keyTask.handle(manager, key),
                (obj, key) -> valueTask.handle(manager, obj, key),
                map
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void getMapMap(RestoreManager manager, BinaryJsonData data, Method getter, Object ref, MappingMode firstMode, MappingMode secondMode, MappingMode thirdMode, MapSupplier supplier) throws InvocationTargetException, IllegalAccessException {
        Map map = (Map) getter.invoke(ref);
        FromStringKey firstKey = getTasks().getFromStringKey(firstMode);
        if(firstKey == null) {
            throw new IllegalArgumentException("unsupported first mode: " + firstMode);
        }
        FromStringKey secondKey = getTasks().getFromStringKey(secondMode);
        if(secondKey == null) {
            throw new IllegalArgumentException("unsupported second mode: " + secondMode);
        }
        GetFromObjectNode thirdTask = getTasks().getGetFromObjectNode(thirdMode);
        if(thirdTask == null) {
            throw new IllegalArgumentException("unsupported third mode: " + thirdMode);
        }

        data.getMapMap(
                key1 -> firstKey.handle(manager, key1),
                key2 -> secondKey.handle(manager, key2),
                (obj, key) -> thirdTask.handle(manager, obj, key),
                map,
                supplier
        );
    }

    //=========================
    //restore
    //=========================

    @Override
    protected T doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        try {
            T object = createInstance();
            for(PrimitiveMultiEntry multiEntry: primitiveEntries) {
                PrimitiveEntry entry = multiEntry.getForRestore(manager.getName());
                if(entry != null) {
                    getPrimitive(data, entry.setter(), object);
                }
            }

            return object;
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RestoreException(e);
        }
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, T object, RestoreManager manager) throws RestoreException {
            try {
                for(NonPrimitiveMultiEntry multiEntry: nonPrimitiveEntries) {
                    NonPrimitiveEntry entry = multiEntry.getForRestore(manager.getName());
                    if(entry != null) {
                        getNonPrimitive(manager, data, entry.setter(), object);
                    }
                }

                for(CollectionMultiEntry multiEntry: collectionMultiEntries) {
                    CollectionEntry entry = multiEntry.getForRestore(manager.getName());
                    if(entry != null) {
                        getCollection(manager, data, entry.setter(), object, entry.mode());
                    }
                }

                for(MapMultiEntry multiEntry: mapMultiEntries) {
                    MapEntry entry = multiEntry.getForRestore(manager.getName());
                    if(entry != null) {
                        getMap(manager, data, entry.setter(), object, entry.keyMode(), entry.valueMode());
                    }
                }

                for(MapMapMultiEntry multiEntry: mapMapMultiEntries) {
                    MapMapEntry entry = multiEntry.getForRestore(manager.getName());
                    if(entry != null) {
                        getMapMap(manager, data, entry.setter(), object, entry.firstMode(), entry.secondMode(), entry.thirdMode(), entry.supplier());
                    }
                }

            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RestoreException(e);
            }
    }

    //==================================================
    //entry
    //==================================================

    //=========================
    //multi
    //=========================

    /**
     * @author Daniel Abitz
     */
    @SuppressWarnings("unused")
    public interface MultiEntry<T extends Entry> extends Iterable<T> {

        T getForPersist(String name);

        T getForRestore(String name);

        Stream<T> stream();
    }

    /**
     * @author Daniel Abitz
     */
    protected static class MultiEntryImpl<T extends Entry> implements MultiEntry<T>, Iterable<T> {

        protected final Map<String, List<T>> persisters = new HashMap<>();
        protected final Map<String, List<T>> restorers = new HashMap<>();

        protected void add(T entry) {
            persisters.computeIfAbsent(entry.getPersisterName(), _name -> new ArrayList<>()).add(entry);
            restorers.computeIfAbsent(entry.getRestorername(), _name -> new ArrayList<>()).add(entry);
        }

        @Override
        public T getForPersist(String name) {
            List<T> list = persisters.get(name);
            return list == null || list.isEmpty()
                    ? null
                    : list.get(0);
        }

        @Override
        public T getForRestore(String name) {
            List<T> list = restorers.get(name);
            return list == null || list.isEmpty()
                    ? null
                    : list.get(0);
        }

        @Override
        public Iterator<T> iterator() {
            return stream().iterator();
        }

        public Stream<T> stream() {
            return persisters.values()
                    .stream()
                    .flatMap(Collection::stream);
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface PrimitiveMultiEntry extends MultiEntry<PrimitiveEntry> {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class PrimitiveMultiEntryImpl extends MultiEntryImpl<PrimitiveEntry> implements PrimitiveMultiEntry {
    }

    /**
     * @author Daniel Abitz
     */
    public interface NonPrimitiveMultiEntry extends MultiEntry<NonPrimitiveEntry> {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class NonPrimitiveMultiEntryImpl extends MultiEntryImpl<NonPrimitiveEntry> implements NonPrimitiveMultiEntry {
    }

    /**
     * @author Daniel Abitz
     */
    public interface CollectionMultiEntry extends MultiEntry<CollectionEntry> {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class CollectionMultiEntryImpl extends MultiEntryImpl<CollectionEntry> implements CollectionMultiEntry {
    }

    /**
     * @author Daniel Abitz
     */
    public interface MapMultiEntry extends MultiEntry<MapEntry> {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class MapMultiEntryImpl extends MultiEntryImpl<MapEntry> implements MapMultiEntry {
    }

    /**
     * @author Daniel Abitz
     */
    public interface MapMapMultiEntry extends MultiEntry<MapMapEntry> {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class MapMapMultiEntryImpl extends MultiEntryImpl<MapMapEntry> implements MapMapMultiEntry {
    }

    //=========================
    //single
    //=========================

    /**
     * @author Daniel Abitz
     */
    public interface Entry {

        String getPersisterName();

        String getRestorername();

        Method getter();

        Method setter();
    }

    /**
     * @author Daniel Abitz
     */
    protected abstract static class EntryImpl implements Entry {

        protected String persisterName;
        protected String restorerName;
        protected Method getter;
        protected Method setter;

        protected void setPersisterName(String persisterName) {
            this.persisterName = persisterName;
        }

        @Override
        public String getPersisterName() {
            return persisterName;
        }

        protected void setRestorerName(String restorerName) {
            this.restorerName = restorerName;
        }

        @Override
        public String getRestorername() {
            return restorerName;
        }

        protected void setGetter(Method getter) {
            this.getter = getter;
        }

        @Override
        public Method getter() {
            return getter;
        }

        protected void setSetter(Method setter) {
            this.setter = setter;
        }

        @Override
        public Method setter() {
            return setter;
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface PrimitiveEntry extends Entry {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class PrimitiveEntryImpl extends EntryImpl implements PrimitiveEntry {

        @Override
        public String toString() {
            return "PrimitiveEntryImpl{" +
                    "persisterName='" + persisterName + '\'' +
                    ", restorerName='" + restorerName + '\'' +
                    ", getter=" + getter +
                    ", setter=" + setter +
                    '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface NonPrimitiveEntry extends Entry {
    }

    /**
     * @author Daniel Abitz
     */
    protected static class NonPrimitiveEntryImpl extends EntryImpl implements NonPrimitiveEntry {

        @Override
        public String toString() {
            return "NonPrimitiveEntryImpl{" +
                    "persisterName='" + persisterName + '\'' +
                    ", restorerName='" + restorerName + '\'' +
                    ", getter=" + getter +
                    ", setter=" + setter +
                    '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface CollectionEntry extends Entry {

        MappingMode mode();
    }

    /**
     * @author Daniel Abitz
     */
    protected static class CollectionEntryImpl extends EntryImpl implements CollectionEntry {

        protected MappingMode mode;

        protected void setMode(MappingMode mode) {
            this.mode = mode;
        }

        @Override
        public MappingMode mode() {
            return mode;
        }

        @Override
        public String toString() {
            return "CollectionEntryImpl{" +
                    "persisterName='" + persisterName + '\'' +
                    ", restorerName='" + restorerName + '\'' +
                    ", getter=" + getter +
                    ", mode=" + mode +
                    '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface MapEntry extends Entry {

        MappingMode keyMode();

        MappingMode valueMode();
    }

    /**
     * @author Daniel Abitz
     */
    protected static class MapEntryImpl extends EntryImpl implements MapEntry {

        protected MappingMode keyMode;
        protected MappingMode valueMode;

        protected void setKeyMode(MappingMode keyMode) {
            this.keyMode = keyMode;
        }

        @Override
        public MappingMode keyMode() {
            return keyMode;
        }

        protected void setValueMode(MappingMode valueMode) {
            this.valueMode = valueMode;
        }

        @Override
        public MappingMode valueMode() {
            return valueMode;
        }

        @Override
        public String toString() {
            return "MapEntryImpl{" +
                    "persisterName='" + persisterName + '\'' +
                    ", restorerName='" + restorerName + '\'' +
                    ", getter=" + getter +
                    ", keyMode=" + keyMode +
                    ", valueMode=" + valueMode +
                    '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface MapMapEntry extends Entry {

        MappingMode firstMode();

        MappingMode secondMode();

        MappingMode thirdMode();

        MapSupplier supplier();
    }

    /**
     * @author Daniel Abitz
     */
    protected static class MapMapEntryImpl extends EntryImpl implements MapMapEntry {

        protected MappingMode firstMode;
        protected MappingMode secondMode;
        protected MappingMode thirdMode;
        protected MapSupplier supplier;

        protected void setFirstMode(MappingMode firstMode) {
            this.firstMode = firstMode;
        }

        @Override
        public MappingMode firstMode() {
            return firstMode;
        }

        protected void setSecondMode(MappingMode secondMode) {
            this.secondMode = secondMode;
        }

        @Override
        public MappingMode secondMode() {
            return secondMode;
        }

        protected void setThirdMode(MappingMode thirdMode) {
            this.thirdMode = thirdMode;
        }

        @Override
        public MappingMode thirdMode() {
            return thirdMode;
        }

        protected void setSupplier(MapSupplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public MapSupplier supplier() {
            return supplier;
        }

        @Override
        public String toString() {
            return "MapMapEntryImpl{" +
                    "persisterName='" + persisterName + '\'' +
                    ", restorerName='" + restorerName + '\'' +
                    ", getter=" + getter +
                    ", firstMode=" + firstMode +
                    ", secondMode=" + secondMode +
                    ", thirdMode=" + thirdMode +
                    ", supplier=" + supplier +
                    '}';
        }
    }
}
