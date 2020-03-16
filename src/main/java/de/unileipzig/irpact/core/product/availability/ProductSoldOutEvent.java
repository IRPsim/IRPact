package de.unileipzig.irpact.core.product.availability;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.Event;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public final class ProductSoldOutEvent implements Event {

    private Product product;
    private ProductAvailability availability;

    public ProductSoldOutEvent(Product product, ProductAvailability availability) {
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
