package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.distattr.AbstractDerivableUnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class SuppliedSpatialDoubleAttributeBase extends AbstractDerivableUnivariateDoubleDistributionAttribute<SpatialDoubleAttribute> implements SuppliedSpatialDoubleAttribute{

    public SuppliedSpatialDoubleAttributeBase() {
    }

    @Override
    public SpatialDoubleAttribute derive(double value) {
        return new SpatialDoubleAttributeBase(getName(), value);
    }
}
