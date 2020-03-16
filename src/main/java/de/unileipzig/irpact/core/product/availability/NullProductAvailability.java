package de.unileipzig.irpact.core.product.availability;

/**
 * Nullinstanz fuer ProductAvailability. Falls ein Produkt noch keine Information hatte, muss
 * diese Klasse verwendet werden. Und sie darf auch nur fuer initialen Nullwert genutzt werden.
 * @author Daniel Abitz
 */
public final class NullProductAvailability implements ProductAvailability {

    public static final String NAME = NullProductAvailability.class.getSimpleName();
    public static final NullProductAvailability INSTANCE = new NullProductAvailability();

    public static ProductAvailability check(ProductAvailability availability) {
        return availability == null
                ? INSTANCE
                : availability;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void decrement() {
    }
}
