package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class PointOfSaleAgentBase extends SpatialInformationAgentBase implements PointOfSaleAgent {

    protected Map<Product, ProductAvailability> productAvailability = new HashMap<>();
    protected Map<Product, Price> productPrices = new HashMap<>();
    protected ProductAvailabilityChangeScheme productAvailabilityChangeScheme;
    protected ProductSoldOutScheme productSoldOutScheme;
    protected ProductPriceChangeScheme productPriceChangeScheme;

    public PointOfSaleAgentBase(
            SimulationEnvironment environment,
            String name,
            double informationAuthority,
            SpatialInformation spatialInformation,
            ProductAvailabilityChangeScheme productAvailabilityChangeScheme,
            ProductSoldOutScheme productSoldOutScheme,
            ProductPriceChangeScheme productPriceChangeScheme) {
        super(environment, name, informationAuthority, spatialInformation);
        this.productAvailabilityChangeScheme = Check.requireNonNull(productAvailabilityChangeScheme, "productAvailabilityChangeScheme");
        this.productSoldOutScheme = Check.requireNonNull(productSoldOutScheme, "productSoldOutScheme");
        this.productPriceChangeScheme = Check.requireNonNull(productPriceChangeScheme, "productPriceChangeScheme");
    }

    @Override
    public PointOfSaleAgentIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Product, ProductAvailability> getProductAvailability() {
        return productAvailability;
    }

    @Override
    public Map<Product, Price> getProductPrices() {
        return productPrices;
    }

    @Override
    public ProductAvailabilityChangeScheme getProductAvailabilityChangeScheme() {
        return productAvailabilityChangeScheme;
    }

    @Override
    public ProductPriceChangeScheme getProductPriceChangeScheme() {
        return productPriceChangeScheme;
    }

    @Override
    public ProductSoldOutScheme getProductSoldOutScheme() {
        return productSoldOutScheme;
    }

    @Override
    public Price requestPrice(Product product) {
        Price price = productPrices.get(product);
        if(price == null) {
            throw new NoSuchElementException("Product not found: " + product.getName());
        }
        return price;
    }

    @Override
    public boolean buyProduct(Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updatePrice(Product product, Price newPrice) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateAvailability(Product product, ProductAvailability newAvailability) {
        throw new UnsupportedOperationException();
    }
}
