package de.unileipzig.irpact.core.spatial.attribute;

import de.unileipzig.irpact.commons.distributionattribut.AbstractDerivableUnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public class SuppliedSpatialDoubleAttributeBase
        extends AbstractDerivableUnivariateDoubleDistributionAttribute<SpatialAttribute>
        implements SuppliedSpatialAttribute {

    public SuppliedSpatialDoubleAttributeBase() {
    }

    @Override
    public SpatialDoubleAttribute derive(double value) {
        return new SpatialDoubleAttribute(getName(), value);
    }
}
