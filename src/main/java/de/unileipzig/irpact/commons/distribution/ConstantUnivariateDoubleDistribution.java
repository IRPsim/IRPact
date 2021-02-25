package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.NameableBase;

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
    public boolean isEqualsSameClass(Object obj) {
        ConstantUnivariateDoubleDistribution other = (ConstantUnivariateDoubleDistribution) obj;
        return name.equals(other.name)
                && value == other.value;
    }
}
