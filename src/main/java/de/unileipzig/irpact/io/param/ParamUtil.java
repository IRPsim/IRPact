package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.util.RuleBuilder;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author Daniel Abitz
 */
public final class ParamUtil {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ParamUtil.class);

    public static final String DATA_DELIMITER = "_";
    public static final String NAME_DELIMITER = "_";

    public static final String UNIT_PIXEL = "[Pixel]";

    public static final String DOMAIN_BOOLEAN = "[0|1]";
    public static final String DOMAIN_CLOSED_0_1 = "[0,1]";
    public static final String DOMAIN_GEQ0 = "[0,)";
    public static final String DOMAIN_G0 = "(0,)";

    public static final Object[] VALUE_TRUE = {"1"};
    public static final Object[] VALUE_FALSE = {"0"};
    public static final Object[] VALUE_ONE = {"1"};
    public static final Object[] VALUE_ZERO = {"0"};
    public static final Object[] VALUE_1000 = {"1000"};
    public static final Object[] VALUE_NEG_ONE = {"-1"};

    private ParamUtil() {
    }

    public static <T> T[] add(T[] arr, T value) {
        T[] newArr = Arrays.copyOf(arr, arr.length + 1);
        newArr[arr.length] = value;
        return newArr;
    }

    public static <T> T[] addAll(T[] target, T[] values) {
        if(values == null || values.length == 0) {
            return target;
        } else {
            T[] newArr = Arrays.copyOf(target, target.length + values.length);
            System.arraycopy(values, 0, newArr, target.length, values.length);
            return newArr;
        }
    }

    public static String getClassNameWithoutClassSuffix(Class<?> c) {
        String name = c.getSimpleName();
        int dot = name.lastIndexOf('.');
        if(dot == -1) {
            return name;
        } else {
            return name.substring(0, dot);
        }
    }

    public static String buildDefaultParameterName(Class<?> c, String field) {
        return Constants.PAR + getClassNameWithoutClassSuffix(c) + "_" + field;
    }

    public static UnaryOperator<String> buildDefaultParameterNameOperator(Class<?> c) {
        return field -> buildDefaultParameterName(c, field);
    }

    public static String buildDefaultScalarName(Class<?> c, String field) {
        return Constants.SCA + getClassNameWithoutClassSuffix(c) + "_" + field;
    }

    public static UnaryOperator<String> buildDefaultScalarNameOperator(Class<?> c) {
        return field -> buildDefaultScalarName(c, field);
    }

    public static Object[] varargs(Object singleton) {
        return new Object[]{singleton};
    }

    public static Object[] varargs(Object... arr) {
        return arr;
    }

    public static String concData(InIRPactEntity first, InIRPactEntity second) {
        return concData(first.getName(), second.getName());
    }

    public static String concData(Nameable first, Nameable second) {
        return concData(first.getName(), second.getName());
    }

    public static String concData(String first, String second) {
        return first + DATA_DELIMITER + second;
    }

    public static String concName(InIRPactEntity first, InIRPactEntity second) {
        return concName(first.getName(), second.getName());
    }

    public static String concName(Nameable first, Nameable second) {
        return concName(first.getName(), second.getName());
    }

    public static String concName(String first, String second) {
        return first + NAME_DELIMITER + second;
    }

    public static String firstPart(String concStr) throws ParsingException {
        return getPart(concStr, 0);
    }

    public static String secondPart(String concStr) throws ParsingException {
        return getPart(concStr, 1);
    }

    private static String getPart(String concStr, int part) throws ParsingException {
        String[] parts = concStr.split(ParamUtil.NAME_DELIMITER);
        if(parts.length != 2) {
            throw new ParsingException("Illegal Name: '" + concStr + "'");
        }
        return parts[part];
    }

    public static <T extends InIRPactEntity> Predicate<T> startWithFilter(InIRPactEntity e) {
        return startWithFilter(e.getName());
    }

    public static <T extends InIRPactEntity> Predicate<T> startWithFilter(Nameable n) {
        return startWithFilter(n.getName());
    }

    public static <T extends InIRPactEntity> Predicate<T> startWithFilter(String name) {
        return n -> {
            if(n == null) {
                return false;
            } else {
                String nn = n.getName();
                if(nn == null) {
                    return false;
                } else {
                    return nn.startsWith(name);
                }
            }
        };
    }

    public static String printClass(Object input) {
        if(input == null) {
            return "null";
        }
        else if(input instanceof Class<?>) {
            return ((Class<?>) input).getName();
        }
        else {
            return input.getClass().getName();
        }
    }

    public static <T> T castTo(Object input, Class<T> c) throws ParsingException {
        if(c.isInstance(input)) {
            return c.cast(input);
        } else {
            throw new ParsingException("type mismatch: " + printClass(input) + " != " + printClass(c));
        }
    }

    public static String onMissing(String name) {
        return "missing entry: '" + name + "'";
    }

    public static String onTooMany(String name, int count) {
        return "too many entries: '" + name + "' (" + count + ")";
    }

    public static String onTooMany(String name, Object[] arr) {
        return onTooMany(name, len(arr));
    }

    public static <T> T[] getNonNullArray(T[] arr, String name) throws ParsingException {
        if(arr == null) {
            throw new ParsingException(onMissing(name));
        }
        return arr;
    }

    public static <T> T[] getNonEmptyArray(T[] arr, String name) throws ParsingException {
        if(arr == null || arr.length == 0) {
            throw new ParsingException(onMissing(name));
        }
        return arr;
    }

    public static <T> T[] getOneElementArray(T[] arr, String name) throws ParsingException {
        if(arr == null || arr.length == 0) {
            throw new ParsingException(onMissing(name));
        }
        if(arr.length > 1) {
            throw new ParsingException(onTooMany(name, arr));
        }
        return arr;
    }

    public static boolean isNotNullAndNotEmpty(Object[] arr) {
        return arr != null && arr.length > 0;
    }

    public static <T> T getInstance(
            T[] arr,
            String name) throws ParsingException {
        return getInstance(arr, onMissing(name), onTooMany(name, arr));
    }

    public static <T> T getInstanceOr(
            T[] arr,
            T ifMissing,
            String msgTooMany) throws ParsingException {
        if(arr == null || arr.length == 0) {
            return ifMissing;
        }
        if(arr.length == 1) {
            return arr[0];
        } else {
            throw new ParsingException(msgTooMany);
        }
    }

    public static <T> T getInstance(
            T[] arr,
            String msgMissing,
            String msgTooMany) throws ParsingException {
        if(arr == null || arr.length == 0) {
            throw new ParsingException(msgMissing);
        }
        if(arr.length == 1) {
            return arr[0];
        } else {
            throw new ParsingException(msgTooMany);
        }
    }

    public static int len(Object[] arr) {
        return arr == null
                ? -1
                : arr.length;
    }

    public static <T extends InIRPactEntity> T getEntityByName(T[] entities, String name) {
        return getEntityByName(entities, name, null);
    }

    public static <T extends InIRPactEntity> T getEntityByName(T[] entities, String name, T defaultEntity) {
        if(entities == null) {
            return defaultEntity;
        }
        for(T entity: entities) {
            if(Objects.equals(name, entity.getName())) {
                return entity;
            }
        }
        return defaultEntity;
    }

    //=========================
    //TreeAnnotationResource
    //=========================

    public static void addPathElement(TreeAnnotationResource res, String dataKey, String priorityKey) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();
        LocalizationData loc = userData.getData();

        TreeAnnotationResource.PathElementBuilder builder = res.newElementBuilder();
        builder = builder.peek(loc.applyPathElementBuilder(dataKey));
        if(priorityKey != null) {
            builder = builder.setEdnPriority(counter.getAndInc(priorityKey));
        }
        builder.putCache(dataKey);
    }

    public static void addPathElement(TreeAnnotationResource res, EdnPath path, String priorityKeyForLastElement) {
        addPathElement(res, path.getLast(), priorityKeyForLastElement);
    }

    public static void addPathElement(TreeAnnotationResource res, EdnPath path) {
        addPathElement(res, path.getLast(), path.getSecondToLast());
    }

    public static TreeAnnotationResource.EntryBuilder computeEntryBuilderIfAbsent(TreeAnnotationResource res, Class<?> c) {
        TreeAnnotationResource.Entry entry = res.getEntry(c);
        if(entry == null) {
            TreeAnnotationResource.EntryBuilder builder = res.newEntryBuilder();
            builder.store(c);
            return builder;
        } else {
            return res.wrapEntryBuilder(entry);
        }
    }

    public static TreeAnnotationResource.EntryBuilder computeEntryBuilderIfAbsent(TreeAnnotationResource res, Class<?> c, String field) {
        TreeAnnotationResource.Entry entry = res.getEntry(c, field);
        if(entry == null) {
            TreeAnnotationResource.EntryBuilder builder = res.newEntryBuilder();
            builder.store(c, field);
            return builder;
        } else {
            return res.wrapEntryBuilder(entry);
        }
    }

    public static void apply(
            TreeAnnotationResource res,
            Class<?> c,
            Consumer<? super TreeAnnotationResource.EntryBuilder> consumer) {
        TreeAnnotationResource.EntryBuilder builder = computeEntryBuilderIfAbsent(res, c);
        builder.peek(consumer);
    }

    public static void apply(
            TreeAnnotationResource res,
            Class<?> c,
            String field,
            Consumer<? super TreeAnnotationResource.EntryBuilder> consumer) {
        TreeAnnotationResource.EntryBuilder builder = computeEntryBuilderIfAbsent(res, c, field);
        builder.peek(consumer);
    }

    public static void setDelta(
            TreeAnnotationResource res,
            Class<?> c) {
        computeEntryBuilderIfAbsent(res, c).setEdnDelta(true);
    }

    public static void setDelta(
            TreeAnnotationResource res,
            Class<?> c,
            String field) {
        computeEntryBuilderIfAbsent(res, c, field).setEdnDelta(true);
    }

    public static void setHidden(
            TreeAnnotationResource res,
            Class<?> c) {
        computeEntryBuilderIfAbsent(res, c).setGamsHidden(true);
    }

    public static void setHidden(
            TreeAnnotationResource res,
            Class<?> c,
            String field) {
        computeEntryBuilderIfAbsent(res, c, field).setGamsHidden(true);
    }

    public static void setDefault(
            TreeAnnotationResource res,
            Class<?> c,
            Object[] defaults) {
        if(defaults != null && defaults.length > 0) {
            computeEntryBuilderIfAbsent(res, c).setGamsDefault(StringUtil.concat(", ", defaults));
        }
    }

    public static void setDefault(
            TreeAnnotationResource res,
            Class<?> c,
            String field,
            Object[] defaults) {
        if(defaults != null && defaults.length > 0) {
            computeEntryBuilderIfAbsent(res, c, field).setGamsDefault(StringUtil.concat(", ", defaults));
        }
    }

    public static void setDefault(
            TreeAnnotationResource res,
            Class<?> c,
            String[] fields,
            Object[] defaults) {
        for(String field: fields) {
            setDefault(res, c, field, defaults);
        }
    }

    public static void setRules(
            TreeAnnotationResource res,
            Class<?> c,
            String field,
            String[] rules) {
        if(rules != null && rules.length > 0) {
            computeEntryBuilderIfAbsent(res, c, field).setGamsRules(rules);
        }
    }

    public static void setRules(
            TreeAnnotationResource res,
            Class<?> c,
            String[] fields,
            RuleBuilder builder) {
        for(String field: fields) {
            setRules(res, c, field, builder.buildFor(field));
        }
    }

    public static void setUnit(
            TreeAnnotationResource res,
            Class<?> c,
            String field,
            String unit) {
        if(unit != null && !unit.isEmpty()) {
            computeEntryBuilderIfAbsent(res, c, field).setGamsUnit(unit);
        }
    }

    public static void setDomain(
            TreeAnnotationResource res,
            Class<?> c,
            String field,
            String domain) {
        if(domain != null) {
            computeEntryBuilderIfAbsent(res, c, field).setGamsDomain(domain);
        }
    }

    public static void setDomain(
            TreeAnnotationResource res,
            Class<?> c,
            String[] fields,
            String domain) {
        for(String field: fields) {
            setDomain(res, c, field, domain);
        }
    }

    public static void addEntry(TreeAnnotationResource res, Class<?> c) {
        IOResources.Data userData = res.getUserDataAs();
        LocalizationData loc = userData.getData();

        res.newEntryBuilder()
                .peek(loc.applyEntryBuilder(c))
                .store(c);
    }

    public static void addEntry(TreeAnnotationResource res, Class<?> c, String field) {
        IOResources.Data userData = res.getUserDataAs();
        LocalizationData loc = userData.getData();

        res.newEntryBuilder()
                .peek(loc.applyEntryBuilder(c, field))
                .store(c, field);
    }

    public static void addEntryWithDefault(TreeAnnotationResource res, Class<?> c, String field, Object[] defaults) {
        addEntry(res, c, field);
        setDefault(res, c, field, defaults);
    }

    public static void addEntryWithDefaultAndDomain(
            TreeAnnotationResource res,
            Class<?> c,
            String field,
            Object[] defaults,
            String domain) {
        addEntry(res, c, field);
        setDefault(res, c, field, defaults);
        setDomain(res, c, field, domain);
    }

    public static void putClassPath(TreeAnnotationResource res, Class<?> c, String... keys) {
        res.putPath(c, res.getCachedElements(keys));
    }

    public static void putClassPath(TreeAnnotationResource res, Class<?> c, EdnPath path) {
        putClassPath(res, c, path.toArrayWithoutRoot());
    }

    public static void putFieldPath(TreeAnnotationResource res, Class<?> c, String field, String... keys) {
        res.putPath(c, field, res.getCachedElements(keys));
    }

    public static void putFieldPath(TreeAnnotationResource res, Class<?> c, String field, EdnPath path) {
        putFieldPath(res, c, field, path.toArrayWithoutRoot());
    }

    public static void putFieldPathAndAddEntry(TreeAnnotationResource res, Class<?> c, String field, String... keys) {
        putFieldPath(res, c, field, keys);
        addEntry(res, c, field);
    }

    public static void putFieldPathAndAddEntry(TreeAnnotationResource res, Class<?> c, String field, EdnPath path) {
        putFieldPath(res, c, field, path);
        addEntry(res, c, field);
    }
}
