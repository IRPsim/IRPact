package de.unileipzig.irpact.v2.commons.distribution;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDoubleDistribution extends NameableBase implements UnivariateDoubleDistribution {

    protected double value;

    public ConstantUnivariateDoubleDistribution() {
    }

    public ConstantUnivariateDoubleDistribution(String name, double value) {
        setName(name);
        setValue(value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double drawDoubleValue() {
        return value;
    }
}
