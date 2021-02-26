package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.interest.InterestSupplyScheme;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public interface ProductInterestSupplyScheme extends InterestSupplyScheme<Product> {

    @Override
    ProductInterest derive();
}
