package de.unileipzig.irpact.commons.distattr;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class UnivariateDoubleDistributionAttributeBase extends NameableBase implements UnivariateDoubleDistributionAttribute {

    protected UnivariateDoubleDistribution distribution;

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDoubleDistribution getValue() {
        return distribution;
    }

    @Override
    public double drawDoubleValue() {
        return distribution.drawDoubleValue();
    }
}
