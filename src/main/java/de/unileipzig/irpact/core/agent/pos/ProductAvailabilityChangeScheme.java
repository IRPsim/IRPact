package de.unileipzig.irpact.core.agent.pos;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;

/**
 * @author Daniel Abitz
 */
public interface ProductAvailabilityChangeScheme extends Scheme {

    void handle(
            PointOfSaleAgent agent,
            Product product,
            ProductAvailability oldAvailability,
            ProductAvailability newAvailability
    );
}
