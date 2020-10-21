package de.unileipzig.irpact.core.product.availability;

import de.unileipzig.irpact.v2.commons.Check;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public final class ProductSoldOut {

    private Product product;
    private ProductAvailability availability;

    public ProductSoldOut(Product product, ProductAvailability availability) {
        this.product = Check.requireNonNull(product, "product");
        this.availability = Check.requireNonNull(availability, "availability");
    }

    public Product getProduct() {
        return product;
    }

    public ProductAvailability getAvailability() {
        return availability;
    }
}
