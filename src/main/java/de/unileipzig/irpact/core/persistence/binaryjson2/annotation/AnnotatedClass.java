package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import de.unileipzig.irpact.core.persistence.binaryjson2.StandardSettings;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.CustomPersistFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.IterableArrayNode;
import de.unileipzig.irpact.core.persistence.binaryjson2.BinaryPersister;
import de.unileipzig.irpact.core.persistence.binaryjson2.PersistHelper;
import de.unileipzig.irpact.core.persistence.binaryjson2.BinaryRestorer;
import de.unileipzig.irpact.core.persistence.binaryjson2.RestoreHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public final class AnnotatedClass implements BinaryPersister<Object>, BinaryRestorer<Object> {

    private static final Comparator<PersistHandler> SORT_PERSISTERS = Comparator.comparingInt(PersistHandler::order);

    private final List<PersistHandler> HANDLERS = new ArrayList<>();
    private final String PERSISTER_NAME;
    private Class<?> t = null;

    public AnnotatedClass() {
        this(StandardSettings.DEFAULT_NAME);
    }

    public AnnotatedClass(String name) {
        this.PERSISTER_NAME = name;
    }

    public AnnotatedClass(Class<?> t) {
        this(StandardSettings.DEFAULT_NAME, t);
    }

    public AnnotatedClass(String name, Class<?> t) {
        this(name);
        scan(t);
    }

    public Class<?> getType() {
        return t;
    }

    public boolean isScanned() {
        return t != null;
    }

    private void validateScanned() {
        if(t == null) {
            throw new IllegalStateException("not scanned");
        }
    }

    private void validate(Object obj) {
        if(obj.getClass() != getType()) {
            throw new IllegalArgumentException("type mismatch: " + obj.getClass() + " != " + getType());
        }
    }

    public void scan(Class<?> t) {
        dispose();

        this.t = t;
        Field[] fields = t.getDeclaredFields();
        for(Field field: fields) {
            if(field.isAnnotationPresent(PersistPrimitive.class)) {
                tryAdd(new PrimitivePersistHandler(field.getAnnotation(PersistPrimitive.class), field));
            }
            if(field.isAnnotationPresent(PersistObject.class)) {
                tryAdd(new ObjectPersistHandler(field.getAnnotation(PersistObject.class), field));
            }
            if(field.isAnnotationPresent(PersistCollection.class)) {
                tryAdd(new CollectionPersistHandler(field.getAnnotation(PersistCollection.class), field));
            }
            if(field.isAnnotationPresent(PersistMap.class)) {
                tryAdd(new MapPersistHandler(field.getAnnotation(PersistMap.class), field));
            }
            if(field.isAnnotationPresent(PersistCustom.class)) {
                tryAdd(new CustomPersistHandler(field.getAnnotation(PersistCustom.class), field));
            }
        }
    }

    private void tryAdd(PersistHandler handler) {
        if(isValid(handler)) {
            HANDLERS.add(handler);
        }
    }

    private boolean isValid(PersistHandler handler) {
        return Objects.equals(PERSISTER_NAME, handler.persister());
    }

    public void sort() {
        HANDLERS.sort(SORT_PERSISTERS);
    }

    public void dispose() {
        HANDLERS.clear();
        t = null;
    }

    @Override
    public void peek(Object input, PersistHelper helper) {
        for(PersistHandler handler: HANDLERS) {
            handler.peek(input, helper);
        }
    }

    @Override
    public void persist(Object input, PersistHelper helper, ArrayNode root) throws Throwable {
        validate(input);
        IterableArrayNode arr = new IterableArrayNode(root);
        arr.clear();
        arr.setClassId(helper, getType());
        for(PersistHandler handler: HANDLERS) {
            handler.persist(input, helper, arr);
        }
    }

    @Override
    public void restore(Object input, RestoreHelper helper, ArrayNode root) throws Throwable {
        validate(input);
        IterableArrayNode arr = new IterableArrayNode(root);
        arr.setIndex(1);
        for(PersistHandler handler: HANDLERS) {
            handler.restore(input, helper, arr);
        }
    }

    //=========================
    // Util
    //=========================

    @SuppressWarnings("unchecked")
    public static <R> R get(Field field, Object obj) throws IllegalAccessException {
        if(field.isAccessible()) {
            return (R) field.get(obj);
        } else {
            field.setAccessible(true);
            try {
                return (R) field.get(obj);
            } finally {
                field.setAccessible(false);
            }
        }
    }

    public static void accept(Field field, Object obj, Accepter2<? super Field, Object> consumer) throws Throwable {
        if(field.isAccessible()) {
            consumer.accept(field, obj);
        } else {
            field.setAccessible(true);
            try {
                consumer.accept(field, obj);
            } finally {
                field.setAccessible(false);
            }
        }
    }

    public static void set(Field field, Object obj, Object value) throws IllegalAccessException {
        if(field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            try {
                field.set(obj, value);
            } finally {
                field.setAccessible(false);
            }
        }
    }

    public static <T> void accept(Field field, Object obj, T value, Accepter3<? super Field, Object, T> consumer) throws Throwable {
        if(field.isAccessible()) {
            consumer.accept(field, obj, value);
        } else {
            field.setAccessible(true);
            try {
                consumer.accept(field, obj, value);
            } finally {
                field.setAccessible(false);
            }
        }
    }

    public static <R> R newInstance(Class<R> c) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return c.getDeclaredConstructor().newInstance();
    }

    public static boolean isPrimitive(Class<?> c) {
        return c.isPrimitive()
                || c == Integer.class
                || c == Long.class
                || c == Double.class
                || c == String.class
                || c == Boolean.class;
    }

    //=========================
    // Helper
    //=========================

    /**
     * @author Daniel Abitz
     */
    @FunctionalInterface
    public interface Accepter2<A, B> {

        void accept(A a, B b) throws Throwable;
    }

    /**
     * @author Daniel Abitz
     */
    @FunctionalInterface
    public interface Accepter3<A, B, C> {

        void accept(A a, B b, C c) throws Throwable;
    }

    /**
     * @author Daniel Abitz
     */
    private static abstract class PersistHandler {

        abstract String persister();

        abstract int order();

        abstract void peek(Object obj, PersistHelper helper);

        abstract void persist(Object obj, PersistHelper helper, IterableArrayNode arr) throws Throwable;

        abstract void restore(Object obj, RestoreHelper helper, IterableArrayNode arr) throws Throwable;
    }

    /**
     * @author Daniel Abitz
     */
    private static class PrimitivePersistHandler extends PersistHandler {

        private PersistPrimitive anno;
        private Field field;

        private PrimitivePersistHandler(PersistPrimitive anno, Field field) {
            this.anno = anno;
            this.field = field;
        }

        @Override
        String persister() {
            return anno.persister();
        }

        @Override
        int order() {
            return anno.order();
        }

        @Override
        void peek(Object obj, PersistHelper helper) {
            //ignore
        }

        @Override
        void persist(Object obj, PersistHelper helper, IterableArrayNode arr) throws Throwable {
            Class<?> fieldType = field.getType();
            //primitive
            if(fieldType == int.class) {
                accept(field, obj, (f, o) -> arr.add(f.getInt(o)));
            }
            else if(fieldType == long.class) {
                accept(field, obj, (f, o) -> arr.add(f.getLong(o)));
            }
            else if(fieldType == double.class) {
                accept(field, obj, (f, o) -> arr.add(f.getDouble(o)));
            }
            else if(fieldType == boolean.class) {
                accept(field, obj, (f, o) -> arr.add(f.getBoolean(o)));
            }
            //boxed
            else if(fieldType == Integer.class) {
                arr.add(get(field, obj));
            }
            else if(fieldType == Long.class) {
                arr.add(get(field, obj));
            }
            else if(fieldType == Double.class) {
                arr.add(get(field, obj));
            }
            else if(fieldType == Boolean.class) {
                arr.add(get(field, obj));
            }
            else if(fieldType == String.class) {
                arr.add(get(field, obj));
            }
            else {
                throw new IllegalArgumentException("unsupported: " + fieldType);
            }
        }

        @Override
        void restore(Object obj, RestoreHelper helper, IterableArrayNode arr) throws Throwable {
            if(arr.peekNull()) {
                set(field, obj, null);
            }
            else if(arr.peekInt()) {
                accept(field, obj, arr.next().asInt(), Field::setInt);
            }
            else if(arr.peekLong()) {
                accept(field, obj, arr.next().asLong(), Field::setLong);
            }
            else if(arr.peekDouble()) {
                accept(field, obj, arr.next().asDouble(), Field::setDouble);
            }
            else if(arr.peekBoolean()) {
                accept(field, obj, arr.next().asBoolean(), Field::setBoolean);
            }
            else if(arr.peekText()) {
                set(field, obj, arr.next().textValue());
            }
            else {
                throw new IllegalArgumentException("unsupported: " + arr.peek().getNodeType());
            }
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class ObjectPersistHandler extends PersistHandler {

        private PersistObject anno;
        private Field field;

        private ObjectPersistHandler(PersistObject anno, Field field) {
            this.anno = anno;
            this.field = field;
        }

        @Override
        String persister() {
            return anno.persister();
        }

        @Override
        int order() {
            return anno.order();
        }

        @Override
        void peek(Object obj, PersistHelper helper) {
            try {
                Object v = get(field, obj);
                helper.peek(v);
            } catch (Throwable t) {
                //ignore
            }
        }

        @Override
        void persist(Object obj, PersistHelper helper, IterableArrayNode arr) throws Throwable {
            Object value = get(field, obj);
            long uid = helper.getUid(value);
            arr.add(uid);
        }

        @Override
        void restore(Object obj, RestoreHelper helper, IterableArrayNode arr) throws Throwable {
            long uid = arr.next().longValue();
            Object value = helper.restore(uid);
            set(field, obj, value);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class CollectionPersistHandler extends PersistHandler {

        private PersistCollection anno;
        private Field field;

        private CollectionPersistHandler(PersistCollection anno, Field field) {
            this.anno = anno;
            this.field = field;
        }

        @Override
        String persister() {
            return anno.persister();
        }

        @Override
        int order() {
            return anno.order();
        }

        @Override
        void peek(Object obj, PersistHelper helper) {
            try {
                Collection<?> c = get(field, obj);
                for(Object e: c) {
                    if(!isPrimitive(e.getClass())) {
                        helper.peek(e);
                    }
                }
            } catch (Throwable t) {
                //ignore
            }
        }

        @Override
        void persist(Object obj, PersistHelper helper, IterableArrayNode arr) throws Throwable {
            Collection<?> c = get(field, obj);
            arr.addColl(helper, c);
        }

        @Override
        void restore(Object obj, RestoreHelper helper, IterableArrayNode arr) throws Throwable {
            Collection<?> c = arr.getCol(
                    newInstance(anno.collSupplier()),
                    newInstance(anno.elementMapper())
            );
            set(field, obj, c);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class MapPersistHandler extends PersistHandler {

        private PersistMap anno;
        private Field field;

        private MapPersistHandler(PersistMap anno, Field field) {
            this.anno = anno;
            this.field = field;
        }

        @Override
        String persister() {
            return anno.persister();
        }

        @Override
        int order() {
            return anno.order();
        }

        @Override
        void peek(Object obj, PersistHelper helper) {
            try {
                Map<?, ?> m = get(field, obj);
                for(Map.Entry<?, ?> e: m.entrySet()) {
                    Object k = e.getKey();
                    if(!isPrimitive(k.getClass())) {
                        helper.peek(k);
                    }
                    Object v = e.getValue();
                    if(!isPrimitive(v.getClass())) {
                        helper.peek(v);
                    }
                }
            } catch (Throwable t) {
                //ignore
            }
        }

        @Override
        void persist(Object obj, PersistHelper helper, IterableArrayNode arr) throws Throwable {
            Map<?, ?> m = get(field, obj);
            arr.addMap(helper, m);
        }

        @Override
        void restore(Object obj, RestoreHelper helper, IterableArrayNode arr) throws Throwable {
            Map<?, ?> m = arr.getMap(
                    newInstance(anno.mapSupplier()),
                    newInstance(anno.keyMapper()),
                    newInstance(anno.valueMapper())
            );
            set(field, obj, m);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class CustomPersistHandler extends PersistHandler {

        private final Map<Class<?>, CustomPersistFunction<?>> FUNCTIONS = new HashMap<>();
        private PersistCustom anno;
        private Field field;

        private CustomPersistHandler(PersistCustom anno, Field field) {
            this.anno = anno;
            this.field = field;
        }

        @Override
        String persister() {
            return anno.persister();
        }

        @Override
        int order() {
            return anno.order();
        }

        @Override
        void peek(Object obj, PersistHelper helper) {
            //not used
        }

        @SuppressWarnings("rawtypes")
        private CustomPersistFunction getFunction(Class<?> c) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            CustomPersistFunction func = FUNCTIONS.get(c);
            if(func == null) {
                func = (CustomPersistFunction) newInstance(c);
                FUNCTIONS.put(c, func);
            }
            return func;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        void persist(Object obj, PersistHelper helper, IterableArrayNode arr) throws Throwable {
            Object value = get(field, obj);
            Class<?> persisterClass = anno.function();
            CustomPersistFunction func = getFunction(persisterClass);
            func.persist(value, helper, arr);
        }

        @SuppressWarnings({"rawtypes"})
        @Override
        void restore(Object obj, RestoreHelper helper, IterableArrayNode arr) throws Throwable {
            Class<?> persisterClass = anno.function();
            CustomPersistFunction func = getFunction(persisterClass);
            Object value = func.restore(helper, arr);
            set(field, obj, value);
        }
    }
}
