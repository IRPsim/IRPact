package de.unileipzig.irpact.core.currency;

/**
 * @author Daniel Abitz
 */
public interface Price {

    default boolean isNull() {
        return false;
    }

    double getValue();
}
