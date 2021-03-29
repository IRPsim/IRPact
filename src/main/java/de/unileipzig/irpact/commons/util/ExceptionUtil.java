package de.unileipzig.irpact.commons.util;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Allows to create exception based on pattern and arguments like logging.
 *
 * @author Daniel Abitz
 */
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
            BiFunction<? super String, ? super Throwable, ? extends T> creator,
            Throwable cause,
            String msg) {
        return creator.apply(msg, cause);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super String, ? super Throwable, ? extends T> creator,
            Throwable cause,
            String pattern,
            Object arg) {
        String msg = StringUtil.format(pattern, arg);
        return creator.apply(msg, cause);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super String, ? super Throwable, ? extends T> creator,
            Throwable cause,
            String pattern,
            Object arg1,
            Object arg2) {
        String msg = StringUtil.format(pattern, arg1, arg2);
        return creator.apply(msg, cause);
    }

    public static <T extends Throwable> T createWithCause(
            BiFunction<? super String, ? super Throwable, ? extends T> creator,
            Throwable cause,
            String pattern,
            Object...args) {
        String msg = StringUtil.format(pattern, args);
        return creator.apply(msg, cause);
    }
}
