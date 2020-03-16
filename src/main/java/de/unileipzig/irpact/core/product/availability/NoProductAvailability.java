package de.unileipzig.irpact.core.product.availability;

/**
 * @author Daniel Abitz
 */
public final class NoProductAvailability implements ProductAvailability {

    public static final String NAME = NoProductAvailability.class.getSimpleName();
    public static final NoProductAvailability INSTANCE = new NoProductAvailability();

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void decrement() {
    }
}
