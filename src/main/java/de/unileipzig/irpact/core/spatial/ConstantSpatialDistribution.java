package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;

import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class ConstantSpatialDistribution extends NameableBase implements SpatialDistribution {

    protected SpatialInformation information;

    public ConstantSpatialDistribution() {
    }

    public ConstantSpatialDistribution(SpatialInformation information) {
        setConstantInformation(information);
    }

    public void setConstantInformation(SpatialInformation information) {
        this.information = information;
    }

    public SpatialInformation getConstantInformation() {
        return information;
    }

    @Override
    public SpatialInformation drawValue() {
        return information.emptyCopy();
    }
}
