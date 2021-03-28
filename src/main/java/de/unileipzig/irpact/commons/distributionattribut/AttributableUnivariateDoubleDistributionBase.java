package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.AbstractAttribute;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class AttributableUnivariateDoubleDistributionBase extends AbstractAttribute implements AttributableUnivariateDoubleDistribution {

    protected UnivariateDoubleDistribution distribution;

    @Override
    public DataType getType() {
        return DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    @Override
    public AttributableUnivariateDoubleDistributionBase copyDistribution() {
        AttributableUnivariateDoubleDistributionBase copy = new AttributableUnivariateDoubleDistributionBase();
        copy.setName(name);
        copy.setUnivariateDoubleDistributionValue(distribution.copyDistribution());
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
    public void setUnivariateDoubleDistributionValue(UnivariateDoubleDistribution value) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDoubleDistribution getUnivariateDoubleDistributionValue() {
        return distribution;
    }

    @Override
    public double drawDoubleValue() {
        return distribution.drawDoubleValue();
    }
}
