package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class UnivariateDoubleDistributionAttribute extends AbstractAttribute {

    protected UnivariateDoubleDistribution value;

    @Override
    public UnivariateDoubleDistributionAttribute copyAttribute() {
        UnivariateDoubleDistributionAttribute copy = new UnivariateDoubleDistributionAttribute();
        copy.setName(getName());
        copy.setUnivariateDoubleDistributionValue(getUnivariateDoubleDistributionValue());
        return copy;
    }

    @Override
    public DataType getType() {
        return DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    @Override
    public UnivariateDoubleDistribution getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = castTo(UnivariateDoubleDistribution.class, value);
    }

    @Override
    public void setUnivariateDoubleDistributionValue(UnivariateDoubleDistribution value) {
        this.value = value;
    }

    @Override
    public UnivariateDoubleDistribution getUnivariateDoubleDistributionValue() {
        return value;
    }
}
