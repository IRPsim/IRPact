package de.unileipzig.irpact.commons;

import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public interface Nameable extends ChecksumComparable {

    String DEFAULT_NAME = "DEFAULT_NAME";

    static <K extends Nameable> Function<? super K, ? extends String> toStringFunction() {
        return Nameable::getName;
    }

    String getName();
}
