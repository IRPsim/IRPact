package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

import java.util.Collection;

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

    @Override
    default Collection<DataType> getDataTypes() {
        return DataType.SET_UNIVARIATE_DOUBLE_DISTRIBUTION;
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
