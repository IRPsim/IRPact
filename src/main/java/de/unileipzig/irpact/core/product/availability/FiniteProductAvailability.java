package de.unileipzig.irpact.core.product.availability;

import de.unileipzig.irpact.commons.annotation.ToDo;

/**
 * @author Daniel Abitz
 */
public final class FiniteProductAvailability implements ProductAvailability {

    public static final String NAME = FiniteProductAvailability.class.getSimpleName();

    private final long INITIAL_COUNT;
    private long count;

    public FiniteProductAvailability(long count) {
        this.INITIAL_COUNT = count;
        this.count = count;
    }

    public long getInitialCount() {
        return INITIAL_COUNT;
    }

    public long getCount() {
        return count;
    }

    @Override
    public boolean isAvailable() {
        return count > 0;
    }

    @ToDo("sync?")
    @Override
    public void decrement() {
        if(count > 0) {
            count--;
        }
    }
}
