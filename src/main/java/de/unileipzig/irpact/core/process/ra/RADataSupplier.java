package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

import java.util.Objects;

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

    public UnivariateDoubleDistribution getDistribution() {
        return dist;
    }

    @Override
    public double drawDoubleValue() {
        return dist.drawDoubleValue();
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getDistribution().getHashCode()
        );
    }
}
