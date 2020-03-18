package de.unileipzig.irpact.core.product.availability;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public final class ProductAvailabilityChange {

    private Product product;
    private ProductAvailability oldAvailability;
    private ProductAvailability newAvailability;

    public ProductAvailabilityChange(
            Product product,
            ProductAvailability oldAvailability,
            ProductAvailability newAvailability) {
        this.product = Check.requireNonNull(product, "product");
        this.oldAvailability = Check.requireNonNull(oldAvailability, "oldAvailability");
        this.newAvailability = Check.requireNonNull(newAvailability, "newAvailability");
    }

    public Product getProduct() {
        return product;
    }

    public ProductAvailability getOldAvailability() {
        return oldAvailability;
    }

    public ProductAvailability getNewAvailability() {
        return newAvailability;
    }
}
