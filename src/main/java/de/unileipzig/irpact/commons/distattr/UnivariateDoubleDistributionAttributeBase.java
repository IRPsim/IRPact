package de.unileipzig.irpact.commons.distattr;

import de.unileipzig.irpact.commons.distribution.DistributionBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class UnivariateDoubleDistributionAttributeBase extends NameableBase implements AttributableUnivariateDoubleDistribution {

    protected UnivariateDoubleDistribution distribution;

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDoubleDistributionAttributeBase copyDistribution() {
        UnivariateDoubleDistributionAttributeBase copy = new UnivariateDoubleDistributionAttributeBase();
        copy.setName(name);
        copy.setDistribution(distribution.copyDistribution());
        return copy;
    }

    @Override
    public UnivariateDoubleDistributionAttributeBase copyAttribute() {
        return copyDistribution();
    }

    @Override
    public UnivariateDoubleDistribution getValue() {
        return distribution;
    }

    @Override
    public void setValue(DistributionBase value) {
        this.distribution = (UnivariateDoubleDistribution) value;
    }

    @Override
    public double drawDoubleValue() {
        return distribution.drawDoubleValue();
    }
}
