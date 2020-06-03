package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.agent.SpatialInformationAgentData;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class PointOfSaleAgentData extends SpatialInformationAgentData {

    public PointOfSaleAgentData() {
    }

    protected Map<Product, ProductAvailability> productAvailability = new HashMap<>();
    public Map<Product, ProductAvailability> getProductAvailability() {
        return productAvailability;
    }
    public void setProductAvailability(Map<Product, ProductAvailability> productAvailability) {
        this.productAvailability = productAvailability;
    }

    protected Map<Product, Price> productPrices = new HashMap<>();
    public Map<Product, Price> getProductPrices() {
        return productPrices;
    }
    public void setProductPrices(Map<Product, Price> productPrices) {
        this.productPrices = productPrices;
    }

    protected NewProductScheme newProductScheme;
    public NewProductScheme getNewProductScheme() {
        return newProductScheme;
    }
    public void setNewProductScheme(NewProductScheme newProductScheme) {
        this.newProductScheme = newProductScheme;
    }

    protected ProductAvailabilityChangeScheme productAvailabilityChangeScheme;
    public ProductAvailabilityChangeScheme getProductAvailabilityChangeScheme() {
        return productAvailabilityChangeScheme;
    }
    public void setProductAvailabilityChangeScheme(ProductAvailabilityChangeScheme productAvailabilityChangeScheme) {
        this.productAvailabilityChangeScheme = productAvailabilityChangeScheme;
    }

    protected ProductSoldOutScheme productSoldOutScheme;
    public ProductSoldOutScheme getProductSoldOutScheme() {
        return productSoldOutScheme;
    }
    public void setProductSoldOutScheme(ProductSoldOutScheme productSoldOutScheme) {
        this.productSoldOutScheme = productSoldOutScheme;
    }

    protected ProductPriceChangeScheme productPriceChangeScheme;
    public ProductPriceChangeScheme getProductPriceChangeScheme() {
        return productPriceChangeScheme;
    }
    public void setProductPriceChangeScheme(ProductPriceChangeScheme productPriceChangeScheme) {
        this.productPriceChangeScheme = productPriceChangeScheme;
    }
}
