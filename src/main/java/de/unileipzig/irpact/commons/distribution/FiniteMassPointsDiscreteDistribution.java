package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.DoublePair;

import java.util.*;

/**
 * @author Daniel Abitz
 */
//mit sortierter liste ! (:
public class FiniteMassPointsDiscreteDistribution
        extends UnivariateDistributionBase
        implements BoundedUnivariateDistribution {

    public static final String NAME = FiniteMassPointsDiscreteDistribution.class.getSimpleName();

    private List<DoublePair> massPoints;
    private double upperBound;
    private double lowerBound;
    private Random rnd;
    private long seed;

    public FiniteMassPointsDiscreteDistribution(
            String name,
            Map<Double, Double> massPoints,
            Random rnd) {
        super(name);
        this.rnd = rnd;
        this.seed = NO_SEED;
        init(massPoints);
    }

    public FiniteMassPointsDiscreteDistribution(
            String name,
            Map<Double, Double> massPoints,
            long seed) {
        super(name);
        this.rnd = newRandom(seed);
        this.seed = seed;
        init(massPoints);
    }

    private void init(Map<Double, Double> inputMassPoints) {
        if(inputMassPoints.isEmpty()) {
            throw new IllegalStateException("no mass points");
        }
        double sum = inputMassPoints.values()
                .stream()
                .mapToDouble(value -> value)
                .sum();
        upperBound = Double.MIN_VALUE;
        lowerBound = Double.MAX_VALUE;
        massPoints = new ArrayList<>(inputMassPoints.size());
        for(Map.Entry<Double, Double> entry: inputMassPoints.entrySet()) {
            double value = entry.getKey();
            massPoints.add(DoublePair.get(value, entry.getValue() / sum));
            if(value > upperBound) {
                upperBound = value;
            }
            if(value < lowerBound) {
                lowerBound = value;
            }
        }
        massPoints.sort(Comparator.comparingDouble(DoublePair::second));
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public double drawValue() {
        final double rndValue = rnd.nextDouble();
        double temp = 0.0;
        double lastValue = 0.0; //dummy, wird sofort ueberschrieben
        for(DoublePair massPoint: massPoints) {
            temp += massPoint.second();
            lastValue = massPoint.first();
            if(rndValue < temp) {
                return lastValue;
            }
        }
        return lastValue;
    }
}
