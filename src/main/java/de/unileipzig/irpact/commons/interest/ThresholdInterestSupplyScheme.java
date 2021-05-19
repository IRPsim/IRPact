package de.unileipzig.irpact.commons.interest;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class ThresholdInterestSupplyScheme<T, U> extends NameableBase implements InterestSupplyScheme<T> {

    protected Map<U, UnivariateDoubleDistribution> distributions;

    public ThresholdInterestSupplyScheme() {
        this(CollectionUtil.newMap());
    }

    public ThresholdInterestSupplyScheme(Map<U, UnivariateDoubleDistribution> distributions) {
        this.distributions = distributions;
    }

    public Map<U, UnivariateDoubleDistribution> getDistributions() {
        return distributions;
    }

    public UnivariateDoubleDistribution getThresholdDistribution(U key) {
        return distributions.get(key);
    }

    public void setThresholdDistribution(U key, UnivariateDoubleDistribution distribution) {
        distributions.put(key, distribution);
    }

    public boolean hasThresholdDistribution(U key) {
        return distributions.containsKey(key);
    }
}
