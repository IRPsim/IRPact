package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

/**
 * @author Daniel Abitz
 */
public class IgnoreProductSoldOut implements ProductSoldOutScheme {

    public static final String NAME = IgnoreProductSoldOut.class.getSimpleName();
    public static final IgnoreProductSoldOut INSTANCE = new IgnoreProductSoldOut();

    @Override
    public void handle(PointOfSaleAgent agent, Product product, ProductAvailability availability) {
    }
}
