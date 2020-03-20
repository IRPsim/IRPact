package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

/**
 * @author Daniel Abitz
 */
public class IgnoreNewProduct implements NewProductScheme {

    public static final String NAME = IgnoreNewProduct.class.getSimpleName();
    public static final IgnoreNewProduct INSTANCE = new IgnoreNewProduct();

    @Override
    public void handle(PointOfSaleAgent agent, Product newProduct) {
    }
}
