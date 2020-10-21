package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class UnivariateDistributionAttributeBase extends NameableBase implements UnivariateDistributionAttribute {

    protected UnivariateDistribution distribution;

    public void setDistribution(UnivariateDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDistribution getDistribution() {
        return distribution;
    }

    @Override
    public double drawValue() {
        return distribution.drawValue();
    }
}
