package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;

import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class ConstantSpatialDistribution extends NameableBase implements SpatialDistribution {

    protected Supplier<SpatialInformation> informationSupplier;

    public ConstantSpatialDistribution() {
    }

    public ConstantSpatialDistribution(Supplier<SpatialInformation> informationSupplier) {
        setInformationSupplier(informationSupplier);
    }

    public void setInformationSupplier(Supplier<SpatialInformation> informationSupplier) {
        this.informationSupplier = informationSupplier;
    }

    public Supplier<SpatialInformation> getInformationSupplier() {
        return informationSupplier;
    }

    @Override
    public SpatialInformation drawValue() {
        return informationSupplier.get();
    }
}
