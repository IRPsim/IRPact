package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public final class ParamUtil {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ParamUtil.class);

    public static final String DELIMITER = "__";

    private ParamUtil() {
    }

    public static String conc(InEntity first, InEntity second) {
        return conc(first.getName(), second.getName());
    }

    public static String conc(Nameable first, Nameable second) {
        return conc(first.getName(), second.getName());
    }

    public static String conc(String first, String second) {
        return first + DELIMITER + second;
    }

    public static String firstPart(String concStr) throws ParsingException {
        return getPart(concStr, 0);
    }

    public static String secondPart(String concStr) throws ParsingException {
        return getPart(concStr, 1);
    }

    private static String getPart(String concStr, int part) throws ParsingException {
        String[] parts = concStr.split(ParamUtil.DELIMITER);
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

    public static String onMissing(String name) {
        return "missing entry: '" + name + "'";
    }

    public static String onTooMany(String name, int count) {
        return "too many entries: '" + name + "' (" + count + ")";
    }

    public static String onTooMany(String name, Object[] arr) {
        return onTooMany(name, len(arr));
    }

    public static <T> T[] getArray(T[] arr, String name) throws ParsingException {
        if(arr == null || arr.length == 0) {
            throw new ParsingException(onMissing(name));
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
}
