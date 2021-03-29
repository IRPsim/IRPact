package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.interest.InterestSupplyScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ProductInterestSupplyScheme extends InterestSupplyScheme<Product> {

    boolean hasThresholdDistribution(ProductGroup group);

    UnivariateDoubleDistribution getThresholdDistribution(ProductGroup group);

    void setThresholdDistribution(ProductGroup group, UnivariateDoubleDistribution distribution);

    boolean addMissing(ProductInterest interest);

    @Override
    ProductInterest derive();
}
