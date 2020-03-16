package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProductPriceChangeScheme extends Scheme {

    void handle(
            PointOfSaleAgent agent,
            Product product,
            Price oldProce,
            Price newPrice
    );
}
