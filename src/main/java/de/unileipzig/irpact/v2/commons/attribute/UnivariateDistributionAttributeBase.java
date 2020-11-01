package de.unileipzig.irpact.v2.commons.attribute;

import de.unileipzig.irpact.v2.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class UnivariateDistributionAttributeBase extends NameableBase implements UnivariateDistributionAttribute {

    protected UnivariateDoubleDistribution distribution;

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
    }

    @Override
    public double drawValue() {
        return distribution.drawDoubleValue();
    }
}
