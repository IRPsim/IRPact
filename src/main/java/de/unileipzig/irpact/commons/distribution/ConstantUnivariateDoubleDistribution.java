package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;

import java.util.Objects;

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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double drawDoubleValue() {
        return value;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(getName(), getValue());
    }
}
