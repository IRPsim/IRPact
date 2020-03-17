package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.attribute.UnivariateDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicProductGroupAttribute extends UnivariateDistributionAttribute implements ProductGroupAttribute {

    public BasicProductGroupAttribute(String name, UnivariateDistribution distribution) {
        super(name, distribution);
    }

    @Override
    public ProductAttribute derive() {
        return derive(drawValue());
    }

    @Override
    public ProductAttribute derive(double fixedValue) {
        return new BasicProductAttribute(this, fixedValue);
    }
}
