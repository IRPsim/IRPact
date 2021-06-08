package de.unileipzig.irpact.develop;

/**
 * @author Daniel Abitz
 */
public final class Dev {

    private Dev() {
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <R> R throwException() throws TodoException {
        throw new TodoException("TODO");
    }
}
