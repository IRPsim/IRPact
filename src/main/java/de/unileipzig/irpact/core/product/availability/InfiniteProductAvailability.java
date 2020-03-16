package de.unileipzig.irpact.core.product.availability;

/**
 * @author Daniel Abitz
 */
public final class InfiniteProductAvailability implements ProductAvailability {

    public static final String NAME = InfiniteProductAvailability.class.getSimpleName();
    public static final InfiniteProductAvailability INSTANCE = new InfiniteProductAvailability();

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void decrement() {
    }
}
