package de.unileipzig.irpact.commons;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface Nameable {

    String getName();

    default boolean hasName(String input) {
        return Objects.equals(getName(), input);
    }
}
