package de.unileipzig.irpact.commons.distributionattribut;

import de.unileipzig.irpact.commons.attribute.AbstractValueAttribute;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class UnivariateDoubleDistributionAttributeBase extends AbstractValueAttribute implements UnivariateDoubleDistributionAttribute {

    protected UnivariateDoubleDistribution distribution;

    @Override
    public DataType getDataType() {
        return DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    @Override
    public UnivariateDoubleDistributionAttributeBase copyDistribution() {
        UnivariateDoubleDistributionAttributeBase copy = new UnivariateDoubleDistributionAttributeBase();
        copy.setName(name);
        copy.setUnivariateDoubleDistributionValue(distribution.copyDistribution());
        return copy;
    }

    @Override
    public UnivariateDoubleDistributionAttributeBase copy() {
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
        this.distribution = value;
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
