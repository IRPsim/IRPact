package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.distribution.DistributionBase;
import de.unileipzig.irpact.commons.util.data.DataType;

/**
 * @author Daniel Abitz
 */
public interface DistributionAttribute extends Attribute<DistributionBase> {

    @Override
    default DataType getType() {
        return DataType.OTHER;
    }

    DistributionBase getValue();
}
