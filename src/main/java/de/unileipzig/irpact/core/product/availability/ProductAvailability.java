package de.unileipzig.irpact.core.product.availability;

/**
 * @author Daniel Abitz
 */
public interface ProductAvailability {

    default boolean isNull() {
        return false;
    }

    boolean isAvailable();

    void decrement();
}
