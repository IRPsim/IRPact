package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedDouble;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@AddToPersist("CHECKSUM UEBERARBEITEN")
public class FiniteMassPointsDiscreteDistribution extends AbstractBoundedUnivariateDoubleDistribution {

    protected List<WeightedDouble> massPoints;
    protected List<WeightedDouble> sortedNormalizedMassPoints;
    protected Rnd rnd;

    public FiniteMassPointsDiscreteDistribution() {
        this(new ArrayList<>());
    }

    public FiniteMassPointsDiscreteDistribution(List<WeightedDouble> massPoints) {
        this.massPoints = massPoints;
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public void add(WeightedDouble point) {
        massPoints.add(point);
        sortedNormalizedMassPoints = null; //reset !
    }

    public void add(double value, double weight) {
        add(new WeightedDouble(value, weight));
    }

    public void init() {
        sortAndNorm();
    }

    private void sortAndNorm() {
        double totalWeight = massPoints.stream()
                .mapToDouble(WeightedDouble::getWeight)
                .sum();
        upperBound = Double.MIN_VALUE;
        lowerBound = Double.MAX_VALUE;
        sortedNormalizedMassPoints = new ArrayList<>();
        for(WeightedDouble mp: massPoints) {
            WeightedDouble normMp = mp.norm(totalWeight);
            sortedNormalizedMassPoints.add(normMp);
            if(normMp.getValue() > upperBound) {
                upperBound = normMp.getValue();
            }
            if(normMp.getValue() < lowerBound) {
                lowerBound = normMp.getValue();
            }
        }
        sortedNormalizedMassPoints.sort(WeightedDouble.getDescendingWeightComparator());
    }

    @Override
    public double drawDoubleValue() {
        if(massPoints.isEmpty()) {
            throw new IllegalStateException("no points");
        }
        if(massPoints.size() == 1) {
            return massPoints.get(0).getValue();
        }
        if(sortedNormalizedMassPoints == null) {
            init();
        }
        final double rndValue = rnd.nextDouble();
        double temp = 0.0;
        double lastValue = 0.0;
        for(WeightedDouble massPoint: sortedNormalizedMassPoints) {
            temp += massPoint.getWeight();
            lastValue = massPoint.getValue();
            if(rndValue < temp) {
                return lastValue;
            }
        }
        return lastValue;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.unsupportedChecksum(getClass());
    }
}