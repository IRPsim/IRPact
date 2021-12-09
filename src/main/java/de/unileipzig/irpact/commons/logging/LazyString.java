package de.unileipzig.irpact.commons.logging;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class LazyString implements LazyToString {

    protected Supplier<? super Object> sub;

    public LazyString(Supplier<? super Object> sub) {
        this.sub = sub;
    }

    @Override
    public String toString() {
        return sub == null
                ? "null"
                : Objects.toString(sub.get());
    }
}
