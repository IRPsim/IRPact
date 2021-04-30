package de.unileipzig.irpact.commons.attribute.v2.dist;

import de.unileipzig.irpact.commons.attribute.v2.AbstractGenericValueAttribute;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public class BasicUnivariateDoubleDistributionAttribute extends AbstractGenericValueAttribute<UnivariateDoubleDistribution> {

    public BasicUnivariateDoubleDistributionAttribute() {
    }

    public BasicUnivariateDoubleDistributionAttribute(String name, UnivariateDoubleDistribution value) {
        setName(name);
        setUnivariateDoubleDistributionValue(value);
    }

    @Override
    public BasicUnivariateDoubleDistributionAttribute copy() {
        BasicUnivariateDoubleDistributionAttribute copy = new BasicUnivariateDoubleDistributionAttribute();
        copy.setName(getName());
        copy.setUnivariateDoubleDistributionValue(getUnivariateDoubleDistributionValue().copyDistribution());
        return copy;
    }

    @Override
    public DataType getDataType() {
        return DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    @Override
    protected Class<UnivariateDoubleDistribution> getTClass() {
        return UnivariateDoubleDistribution.class;
    }

    public void setUnivariateDoubleDistributionValue(UnivariateDoubleDistribution value) {
        this.value = value;
    }

    public UnivariateDoubleDistribution getUnivariateDoubleDistributionValue() {
        return value;
    }
}
