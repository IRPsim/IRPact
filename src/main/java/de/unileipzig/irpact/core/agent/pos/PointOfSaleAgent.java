package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.commons.annotation.Idea;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public interface PointOfSaleAgent extends SpatialInformationAgent {

    Map<Product, ProductAvailability> getProductAvailability();

    Map<Product, Price> getProductPrices();

    ProductAvailabilityChangeScheme getProductAvailabilityChangeScheme();

    ProductPriceChangeScheme getProductPriceChangeScheme();

    ProductSoldOutScheme getProductSoldOutScheme();

    Price requestPrice(Product product);

    boolean buyProduct(Product product);

    @Idea("Scheme einbauen, welches den uebergebenden Preis anpasst, damit POS Gewinn macht")
    void updatePrice(Product product, Price newPrice);

    void updateAvailability(Product product, ProductAvailability newAvailability);
}
