package de.unileipzig.irpact.core.attribute;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;

/**
 * @author Daniel Abitz
 */
public class UnivariateDistributionAttribute implements UnivariateAttribute {

    private String name;
    private UnivariateDistribution distribution;

    public UnivariateDistributionAttribute(String name, UnivariateDistribution distribution) {
        this.name = Check.requireNonNull(name, "name");
        this.distribution = Check.requireNonNull(distribution, "distribution");
    }

    @Override
    public UnivariateDistribution getDistribution() {
        return distribution;
    }

    @Override
    public String getName() {
        return name;
    }
}
