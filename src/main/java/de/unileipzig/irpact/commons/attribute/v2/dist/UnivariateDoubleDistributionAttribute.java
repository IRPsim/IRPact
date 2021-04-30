package de.unileipzig.irpact.commons.attribute.v2.dist;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * Combines attribute and univariate double distribution.
 *
 * @author Daniel Abitz
 */
public interface UnivariateDoubleDistributionAttribute extends UnivariateDistributionAttribute<Number>, UnivariateDoubleDistribution {

    @Override
    default DataType getDataType() {
        return DataType.UNIVARIATE_DOUBLE_DISTRIBUTION;
    }

    @Override
    UnivariateDoubleDistribution getValue();
}
