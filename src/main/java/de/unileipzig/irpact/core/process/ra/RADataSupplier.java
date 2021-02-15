package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public class RADataSupplier extends NameableBase implements UnivariateDoubleDistribution {

    protected UnivariateDoubleDistribution dist;

    public RADataSupplier() {
    }

    public void setDistribution(UnivariateDoubleDistribution dist) {
        this.dist = dist;
    }

    @Override
    public String getName() {
        return dist.getName();
    }

    @Override
    public double drawDoubleValue() {
        return dist.drawDoubleValue();
    }
}
