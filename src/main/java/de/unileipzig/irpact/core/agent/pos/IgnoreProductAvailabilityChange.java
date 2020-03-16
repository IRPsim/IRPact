package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

/**
 * @author Daniel Abitz
 */
public class IgnoreProductAvailabilityChange implements ProductAvailabilityChangeScheme {

    public static final String NAME = IgnoreProductAvailabilityChange.class.getSimpleName();
    public static final IgnoreProductAvailabilityChange INSTANCE = new IgnoreProductAvailabilityChange();

    @Override
    public void handle(
            PointOfSaleAgent agent,
            Product product,
            ProductAvailability oldAvailability,
            ProductAvailability newAvailability) {
    }
}
