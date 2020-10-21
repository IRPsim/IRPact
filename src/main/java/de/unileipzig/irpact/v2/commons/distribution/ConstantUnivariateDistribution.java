package de.unileipzig.irpact.v2.commons.distribution;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDistribution extends NameableBase implements UnivariateDistribution {

    protected double value;

    public ConstantUnivariateDistribution() {
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double drawValue() {
        return value;
    }
}
