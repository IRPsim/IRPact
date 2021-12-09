package de.unileipzig.irpact.core.product.interest;

import de.unileipzig.irpact.commons.interest.Interest;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;

/**
 * @author Daniel Abitz
 */
public interface ProductInterest extends Interest<Product> {

    boolean hasThreshold(ProductGroup group);

    double getThreshold(ProductGroup group);

    void setThreshold(ProductGroup group, double threshold);

    String printInfo(Product product);
}
