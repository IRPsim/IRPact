package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public class ThresholdAwarenessSupplyScheme<T> extends NameableBase implements AwarenessSupplyScheme<T> {

    protected UnivariateDoubleDistribution distribution;

    public ThresholdAwarenessSupplyScheme() {
    }

    public UnivariateDoubleDistribution getDistribution() {
        return distribution;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public ThresholdAwareness<T> derive() {
        ThresholdAwareness<T> awareness = new ThresholdAwareness<>();
        awareness.setThreshold(distribution.drawDoubleValue());
        return awareness;
    }
}
