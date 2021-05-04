package de.unileipzig.irpact.commons.attribute.v3;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public interface UnivariateDoubleDistributionAttribute extends ValueAttribute<UnivariateDoubleDistribution> {

    //=========================
    //Value
    //=========================

    @Override
    default boolean isDataType(DataType dataType) {
        return dataType == DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    //=========================
    //Attribute
    //=========================

    @Override
    default UnivariateDoubleDistributionAttribute asValueAttribute() {
        return this;
    }

    //=========================
    //special
    //=========================
}
