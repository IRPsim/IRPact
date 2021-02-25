package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.distattr.DerivableUnivariateDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductGroupAttribute extends DerivableUnivariateDistributionAttribute<ProductAttribute> {

    ProductAttribute derive(String name, double value);
}
