package de.unileipzig.irpact.core.product.price;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public final class ProductPriceChange {

    private Product product;
    private Price oldPrice;
    private Price newPrice;

    public ProductPriceChange(
            Product product,
            Price oldPrice,
            Price newPrice) {
        this.product = Check.requireNonNull(product, "product");
        this.oldPrice = Check.requireNonNull(oldPrice, "oldPrice");
        this.newPrice = Check.requireNonNull(newPrice, "newPrice");
    }

    public Product getProduct() {
        return product;
    }

    public Price getOldPrice() {
        return oldPrice;
    }

    public Price getNewPrice() {
        return newPrice;
    }
}
