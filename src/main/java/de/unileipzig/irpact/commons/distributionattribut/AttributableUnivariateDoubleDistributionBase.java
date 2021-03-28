package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.AbstractAttribute;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class AttributableUnivariateDoubleDistributionBase extends AbstractAttribute implements AttributableUnivariateDoubleDistribution {

    protected UnivariateDoubleDistribution distribution;

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public DataType getType() {
        return DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    @Override
    public AttributableUnivariateDoubleDistributionBase copyDistribution() {
        AttributableUnivariateDoubleDistributionBase copy = new AttributableUnivariateDoubleDistributionBase();
        copy.setName(name);
        copy.setDistribution(distribution.copyDistribution());
        return copy;
    }

    @Override
    public AttributableUnivariateDoubleDistributionBase copyAttribute() {
        return copyDistribution();
    }

    @Override
    public UnivariateDoubleDistribution getValue() {
        return distribution;
    }

    @Override
    public void setValue(Object value) {
        this.distribution = castTo(UnivariateDoubleDistribution.class, value);
    }

    @Override
    public double drawDoubleValue() {
        return distribution.drawDoubleValue();
    }
}
