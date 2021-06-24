package de.unileipzig.irpact.jadex.persistance.binary.annotation;

import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.data.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class GenericPR<T> extends BinaryPRBase<T> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GenericPR.class);

    private final Class<T> c;

    private final List<PrimitiveMultiEntry> primitiveEntries = new ArrayList<>();
    private final List<NonPrimitiveMultiEntry> nonPrimitiveEntries = new ArrayList<>();

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
        if(!isPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is not primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getDeclaringClass().getName());
        }

        String setter = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        if(!hasSetMethod(c, setter, annotatedField.getDeclaringClass())) {
            throw new IRPactException("setter '{}' not found for {}#{}", setter, c.getName(), annotatedField.getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getDeclaringClass())) {
            throw new IRPactException("getter '{}' not found for {}#{}", setter, c.getName(), annotatedField.getName());
        }
    }

    //=========================
    //PrimitiveBinaryPersist
    //=========================

    private static void validate(NonPrimitiveBinaryPersisters annotations, Class<?> c, Field annotatedField) throws IRPactException {
        for(NonPrimitiveBinaryPersist annotation: annotations.value()) {
            validate(annotation, c, annotatedField);
        }
    }

    private static void validate(NonPrimitiveBinaryPersist annotation, Class<?> c, Field annotatedField) throws IRPactException {
        if(isPrimitive(annotatedField)) {
            throw new IRPactException("field '{}#{}' is primitive, type: {}", c.getName(), annotatedField.getName(), annotatedField.getDeclaringClass().getName());
        }

        String setter = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        if(!hasSetMethod(c, setter, annotatedField.getDeclaringClass())) {
            throw new IRPactException("setter '{}' not found for {}#{}", setter, c.getName(), annotatedField.getName());
        }

        String getter = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        if(!hasGetMethod(c, getter, annotatedField.getDeclaringClass())) {
            throw new IRPactException("getter '{}' not found for {}#{}", setter, c.getName(), annotatedField.getName());
        }
    }

    //==================================================
    //init
    //==================================================

    private void initalize() throws IRPactException {
        try {
            initalizeFields();
        } catch (NoSuchMethodException e) {
            throw new IRPactException(e);
        }
    }

    private void initalizeFields() throws NoSuchMethodException {
        for(Field field: getType().getFields()) {
            if(field.isAnnotationPresent(PrimitiveBinaryPersisters.class)) {
                PrimitiveBinaryPersisters annotation = field.getDeclaredAnnotation(PrimitiveBinaryPersisters.class);
                PrimitiveMultiEntry multiEntry = handle(annotation, field);
                primitiveEntries.add(multiEntry);
            }
            else if(field.isAnnotationPresent(NonPrimitiveBinaryPersisters.class)) {
                NonPrimitiveBinaryPersisters annotation = field.getDeclaredAnnotation(NonPrimitiveBinaryPersisters.class);
                NonPrimitiveMultiEntry multiEntry = handle(annotation, field);
                nonPrimitiveEntries.add(multiEntry);
            }
        }
    }

    private PrimitiveMultiEntry handle(PrimitiveBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        PrimitiveMultiEntryImpl multiEntry = new PrimitiveMultiEntryImpl();
        for(PrimitiveBinaryPersist annotation: annotions.value()) {
            PrimitiveEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    private PrimitiveEntry handle(PrimitiveBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        PrimitiveEntryImpl entry = new PrimitiveEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        String setterName = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        Method setter = getType().getDeclaredMethod(setterName, annotatedField.getDeclaringClass());
        entry.setSetter(setter);

        return entry;
    }

    private NonPrimitiveMultiEntry handle(NonPrimitiveBinaryPersisters annotions, Field annotatedField) throws NoSuchMethodException {
        NonPrimitiveMultiEntryImpl multiEntry = new NonPrimitiveMultiEntryImpl();
        for(NonPrimitiveBinaryPersist annotation: annotions.value()) {
            NonPrimitiveEntry entry = handle(annotation, annotatedField);
            multiEntry.add(entry);
        }
        return multiEntry;
    }

    private NonPrimitiveEntry handle(NonPrimitiveBinaryPersist annotation, Field annotatedField) throws NoSuchMethodException {
        NonPrimitiveEntryImpl entry = new NonPrimitiveEntryImpl();
        entry.setPersisterName(annotation.persisterName());
        entry.setRestorerName(annotation.restorerName());

        String getterName = isValid(annotation.getter()) ? annotation.getter() : getGetterName(annotatedField);
        Method getter = getType().getDeclaredMethod(getterName);
        entry.setGetter(getter);

        String setterName = isValid(annotation.setter()) ? annotation.setter() : getSetterName(annotatedField);
        Method setter = getType().getDeclaredMethod(setterName, annotatedField.getDeclaringClass());
        entry.setSetter(setter);

        return entry;
    }

    //==================================================
    //persist util
    //==================================================

    private static void putPrimitive(BinaryJsonData data, Method getter, Object ref) throws InvocationTargetException, IllegalAccessException {
        Class<?> type = getter.getReturnType();
        if(type == int.class) {
            int result = (int) getter.invoke(ref);
            data.putInt(result);
        }
        else if(type == long.class) {
            long result = (long) getter.invoke(ref);
            data.putLong(result);
        }
        else if(type == double.class) {
            double result = (double) getter.invoke(ref);
            data.putDouble(result);
        }
        else if(type == String.class) {
            String result = (String) getter.invoke(ref);
            data.putText(result);
        }
        else {
            throw new IRPactIllegalArgumentException("unsupported return type: {}", getter.getReturnType());
        }
    }

    private static void prepareNonPrimitive(PersistManager manager, Method getter, Object ref) throws InvocationTargetException, IllegalAccessException, PersistException {
        Object result = getter.invoke(ref);
        manager.prepare(result);
    }

    private static void putNonPrimitive(PersistManager manager, BinaryJsonData data, Method getter, Object ref) throws PersistException, InvocationTargetException, IllegalAccessException {
        Object result = getter.invoke(ref);
        long id = manager.ensureGetUID(result);
        data.putLong(id);
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
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new PersistException(e);
        }
    }

    //==================================================
    //restore util
    //==================================================

    private static Class<?> getParameterType(Method setter) {
        return setter.getParameterTypes()[0];
    }

    private static void getPrimitive(BinaryJsonData data, Method setter, Object ref) throws InvocationTargetException, IllegalAccessException {
        Class<?> type = getParameterType(setter);
        if(type == int.class) {
            setter.invoke(ref, data.getInt());
        }
        else if(type == long.class) {
            setter.invoke(ref, data.getLong());
        }
        else if(type == double.class) {
            setter.invoke(ref, data.getDouble());
        }
        else if(type == String.class) {
            setter.invoke(ref, data.getText());
        }
        else {
            throw new IRPactIllegalArgumentException("unsupported return type: {}", setter.getReturnType());
        }
    }

    private static void getNonPrimitive(RestoreManager manager, BinaryJsonData data, Method setter, Object ref) throws InvocationTargetException, IllegalAccessException, RestoreException {
        long id = data.getLong();
        Object obj = manager.ensureGet(id);
        setter.invoke(ref, obj);
    }

    private T createInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return getType().getDeclaredConstructor()
                .newInstance();
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
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RestoreException(e);
            }
    }

    //==================================================
    //helper
    //==================================================

    /**
     * @author Daniel Abitz
     */
    public interface MultiEntry {

        Entry getForPersist(String name);

        Entry getForRestore(String name);
    }

    /**
     * @author Daniel Abitz
     */
    private static class MultiEntryImpl<T extends Entry> implements MultiEntry {

        protected final List<T> entries = new ArrayList<>();

        protected void add(T entry) {
            entries.add(entry);
        }

        @Override
        public T getForPersist(String name) {
            for(T entry: entries) {
                if(isIgnoreOrEquals(entry.getPersisterName(), name)) {
                    return entry;
                }
            }
            return null;
        }

        @Override
        public T getForRestore(String name) {
            for(T entry: entries) {
                if(isIgnoreOrEquals(entry.getRestorername(), name)) {
                    return entry;
                }
            }
            return null;
        }
    }

    /**
     * @author Daniel Abitz
     */
    public interface PrimitiveMultiEntry extends MultiEntry {

        @Override
        PrimitiveEntry getForPersist(String name);

        @Override
        PrimitiveEntry getForRestore(String name);
    }

    /**
     * @author Daniel Abitz
     */
    private static class PrimitiveMultiEntryImpl extends MultiEntryImpl<PrimitiveEntry> implements PrimitiveMultiEntry {
    }

    /**
     * @author Daniel Abitz
     */
    public interface NonPrimitiveMultiEntry extends MultiEntry {

        @Override
        NonPrimitiveEntry getForPersist(String name);

        @Override
        NonPrimitiveEntry getForRestore(String name);
    }

    /**
     * @author Daniel Abitz
     */
    private static class NonPrimitiveMultiEntryImpl extends MultiEntryImpl<NonPrimitiveEntry> implements NonPrimitiveMultiEntry {
    }

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
    private abstract static class EntryImpl implements Entry {

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
    private static class PrimitiveEntryImpl extends EntryImpl implements PrimitiveEntry {
    }

    /**
     * @author Daniel Abitz
     */
    public interface NonPrimitiveEntry extends Entry {
    }

    /**
     * @author Daniel Abitz
     */
    private static class NonPrimitiveEntryImpl extends EntryImpl implements NonPrimitiveEntry {
    }
}
