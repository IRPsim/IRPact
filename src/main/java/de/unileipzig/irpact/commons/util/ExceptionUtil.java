package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.develop.Todo;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Allows to create exception based on pattern and arguments like logging.
 *
 * @author Daniel Abitz
 */
@Todo("IRPactException Varianten mit pattern support einbauen und diese Klasse schritt fuer schritt abbauen")
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static <T extends Throwable> T create(
            Function<? super String, ? extends T> creator,
            String msg) {
        return creator.apply(msg);
    }

    public static <T extends Throwable> T create(
            Function<? super String, ? extends T> creator,
            String pattern,
            Object arg) {
        String msg = StringUtil.format(pattern, arg);
        return creator.apply(msg);
    }

    public static <T extends Throwable> T create(
            Function<? super String, ? extends T> creator,
            String pattern,
            Object arg1,
            Object arg2) {
        String msg = StringUtil.format(pattern, arg1, arg2);
        return creator.apply(msg);
    }

    public static <T extends Throwable> T create(
            Function<? super String, ? extends T> creator,
            String pattern,
            Object...args) {
        String msg = StringUtil.format(pattern, args);
        return creator.apply(msg);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super Throwable, ? super String, ? extends T> creator,
            Throwable cause,
            String msg) {
        return creator.apply(cause, msg);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super Throwable, ? super String, ? extends T> creator,
            Throwable cause,
            String pattern,
            Object arg) {
        String msg = StringUtil.format(pattern, arg);
        return creator.apply(cause, msg);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super Throwable, ? super String, ? extends T> creator,
            Throwable cause,
            String pattern,
            Object arg1,
            Object arg2) {
        String msg = StringUtil.format(pattern, arg1, arg2);
        return creator.apply(cause, msg);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super Throwable, ? super String, ? extends T> creator,
            Throwable cause,
            String pattern,
            Object...args) {
        String msg = StringUtil.format(pattern, args);
        return creator.apply(cause, msg);
    }
}
