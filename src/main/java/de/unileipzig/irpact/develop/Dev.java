package de.unileipzig.irpact.develop;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class Dev {

    private Dev() {
    }

    public static <R> R throwException() throws TodoException {
        throw new TodoException("TODO");
    }

    public static <R> R throwUnsupported() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("currently not supported");
    }
}
