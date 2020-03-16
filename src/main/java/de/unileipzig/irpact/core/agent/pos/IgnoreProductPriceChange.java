package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class IgnoreProductPriceChange implements ProductPriceChangeScheme {

    public static final String NAME = IgnoreProductPriceChange.class.getSimpleName();
    public static final IgnoreProductPriceChange INSTANCE = new IgnoreProductPriceChange();

    @Override
    public void handle(PointOfSaleAgent agent, Product product, Price oldProce, Price newPrice) {
    }
}
