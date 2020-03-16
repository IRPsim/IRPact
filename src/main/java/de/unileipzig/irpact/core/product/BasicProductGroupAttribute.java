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
}
