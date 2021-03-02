package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public class ThresholdInterestSupplyScheme<T> extends NameableBase implements InterestSupplyScheme<T> {

    protected UnivariateDoubleDistribution distribution;

    public ThresholdInterestSupplyScheme() {
    }

    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public ThresholdInterest<T> derive() {
        ThresholdInterest<T> awareness = new ThresholdInterest<>();
        awareness.setThreshold(distribution.drawDoubleValue());
        return awareness;
    }
}
