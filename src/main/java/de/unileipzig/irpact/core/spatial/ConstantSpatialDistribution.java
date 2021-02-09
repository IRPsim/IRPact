package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class ConstantSpatialDistribution extends NameableBase implements SpatialDistribution {

    protected SpatialInformation information;

    public ConstantSpatialDistribution() {
    }

    public ConstantSpatialDistribution(SpatialInformation information) {
        setInformation(information);
    }

    public void setInformation(SpatialInformation information) {
        this.information = information;
    }

    public SpatialInformation getInformation() {
        return information;
    }

    @Override
    public SpatialInformation drawValue() {
        return information;
    }
}
