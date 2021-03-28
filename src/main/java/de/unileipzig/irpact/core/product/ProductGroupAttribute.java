package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.attribute.GroupAttribute;
import de.unileipzig.irpact.commons.distributionattribut.DerivableUnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public interface ProductGroupAttribute extends GroupAttribute, DerivableUnivariateDoubleDistributionAttribute<ProductAttribute> {

    ProductAttribute derive(String name, double value);
}
