package de.unileipzig.irpact.commons.log;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class LazyPrinter implements LazyToString {

    protected Supplier<? extends CharSequence> supp;

    public LazyPrinter(Supplier<? extends CharSequence> supp) {
        this.supp = supp;
    }

    public static LazyPrinter printArray(Object[] arr) {
        return new LazyPrinter(() -> Arrays.toString(arr));
    }

    public static LazyPrinter of(Supplier<? extends CharSequence> supp) {
        return new LazyPrinter(supp);
    }

    @Override
    public String toString() {
        if(supp == null) {
            return "null";
        }
        CharSequence csq = supp.get();
        return csq == null
                ? "null"
                : csq.toString();
    }
}
