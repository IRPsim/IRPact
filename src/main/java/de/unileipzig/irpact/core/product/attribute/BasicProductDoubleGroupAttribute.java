package de.unileipzig.irpact.core.product.attribute;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicProductDoubleGroupAttribute
        extends AbstractProductGroupAttribute
        implements ProductDoubleGroupAttribute {

    protected UnivariateDoubleDistribution distribution;

    @Override
    public BasicProductDoubleGroupAttribute copy() {
        BasicProductDoubleGroupAttribute copy = new BasicProductDoubleGroupAttribute();
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        copy.setDistribution(getDistribution().copyDistribution());
        return copy;
    }

    @Override
    public BasicProductDoubleAttribute derive() {
        double value = getDistribution().drawDoubleValue();
        return derive(value);
    }

    @Override
    public BasicProductDoubleAttribute derive(double value) {
        return derive(getName(), value);
    }

    @Override
    public BasicProductDoubleAttribute derive(Number input) {
        return derive(getName(), input);
    }

    @Override
    public BasicProductDoubleAttribute derive(String str, Number value) {
        return derive(str, value.doubleValue());
    }

    @Override
    public BasicProductDoubleAttribute derive(String name, double value) {
        BasicProductDoubleAttribute attr = new BasicProductDoubleAttribute();
        attr.setName(name);
        attr.setGroup(this);
        attr.setArtificial(isArtificial());
        attr.setDoubleValue(value);
        return attr;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
    }

    @Override
    public double drawDoubleValue() {
        return distribution.drawDoubleValue();
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                getDistribution()
        );
    }
}
