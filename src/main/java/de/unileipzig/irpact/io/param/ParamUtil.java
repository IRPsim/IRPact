package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public final class ParamUtil {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ParamUtil.class);

    public static final String DATA_DELIMITER = "_";
    public static final String NAME_DELIMITER = "__";

    private ParamUtil() {
    }

    public static String concData(InEntity first, InEntity second) {
        return concData(first.getName(), second.getName());
    }

    public static String concData(Nameable first, Nameable second) {
        return concData(first.getName(), second.getName());
    }

    public static String concData(String first, String second) {
        return first + DATA_DELIMITER + second;
    }

    public static String concName(InEntity first, InEntity second) {
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

    public static <T extends InEntity> Predicate<T> startWithFilter(InEntity e) {
        return startWithFilter(e.getName());
    }

    public static <T extends InEntity> Predicate<T> startWithFilter(Nameable n) {
        return startWithFilter(n.getName());
    }

    public static <T extends InEntity> Predicate<T> startWithFilter(String name) {
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

    public static <T> T getInstance(
            T[] arr,
            String name) throws ParsingException {
        return getInstance(arr, onMissing(name), onTooMany(name, arr));
    }

    public static <T> T getInstance(
            T[] arr,
            String msgMissing,
            String msgTooMany) throws ParsingException {
        if(arr == null || arr.length == 0) {
            throw new ParsingException(msgMissing);
        }
        if(arr.length > 1) {
            throw new ParsingException(msgTooMany);
        }
        return arr[0];
    }

    public static int len(Object[] arr) {
        return arr == null
                ? -1
                : arr.length;
    }


    //=========================
    //TreeAnnotationResource
    //=========================

    public static void addPathElement(TreeAnnotationResource res, String dataKey, String priorityKey) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();
        LocData loc = userData.getData();

        TreeAnnotationResource.PathElementBuilder builder = res.newElementBuilder();
        builder = builder.peek(loc.applyPathElementBuilder(dataKey));
        if(priorityKey != null) {
            builder = builder.setEdnPriority(counter.getAndInc(priorityKey));
        }
        builder.putCache(dataKey);
    }

    public static void addEntry(TreeAnnotationResource res, Class<?> c) {
        IOResources.Data userData = res.getUserDataAs();
        LocData loc = userData.getData();

        res.newEntryBuilder()
                .peek(loc.applyEntryBuilder(c))
                .store(c);
    }

    public static void addEntry(TreeAnnotationResource res, Class<?> c, String field) {
        IOResources.Data userData = res.getUserDataAs();
        LocData loc = userData.getData();

        res.newEntryBuilder()
                .peek(loc.applyEntryBuilder(c, field))
                .store(c, field);
    }

    public static void putClassPath(TreeAnnotationResource res, Class<?> c, String... keys) {
        res.putPath(c, res.getCachedElements(keys));
    }

    public static void putFieldPath(TreeAnnotationResource res, Class<?> c, String field, String... keys) {
        res.putPath(c, field, res.getCachedElements(keys));
    }

    public static void putFieldPathAndAddEntry(TreeAnnotationResource res, Class<?> c, String field, String... keys) {
        putFieldPath(res, c, field, keys);
        addEntry(res, c, field);
    }
}
