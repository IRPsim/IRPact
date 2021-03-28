package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.Distribution;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAttribute extends NameableBase implements Attribute {

    protected <T> T castTo(Class<T> type, Object input) {
        if(input == null) {
            return null;
        }
        if(type.isInstance(input)) {
            return type.cast(input);
        }
        throw new IllegalArgumentException("wrong input type");
    }

    //=========================
    //special
    //=========================

    @Override
    public double getDoubleValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDoubleValue(double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStringValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStringValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnivariateDoubleDistribution getUnivariateDoubleDistributionValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUnivariateDoubleDistributionValue(UnivariateDoubleDistribution value) {
        throw new UnsupportedOperationException();
    }
}
