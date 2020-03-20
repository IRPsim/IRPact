package de.unileipzig.irpact.commons.distribution;

import java.util.Map;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
//wieso? weil ich es kann (:
public class DynamicFiniteMassPointsDiscreteDistribution
        extends UnivariateDistributionBase
        implements BoundedUnivariateDistribution {

    public static final String NAME = DynamicFiniteMassPointsDiscreteDistribution.class.getSimpleName();

    private Map<Double, Double> massPoints;
    private Random rnd;
    private long seed;

    public DynamicFiniteMassPointsDiscreteDistribution(
            String name,
            Map<Double, Double> massPoints,
            Random rnd) {
        super(name);
        this.massPoints = massPoints;
        this.rnd = rnd;
        this.seed = NO_SEED;
    }

    public DynamicFiniteMassPointsDiscreteDistribution(
            String name,
            Map<Double, Double> massPoints,
            long seed) {
        super(name);
        this.massPoints = massPoints;
        this.rnd = newRandom(seed);
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public double getLowerBound() {
        if(massPoints.isEmpty()) {
            throw new IllegalStateException("no mass points");
        }
        return massPoints.keySet()
                .stream()
                .mapToDouble(value -> value)
                .min()
                .getAsDouble();
    }

    @Override
    public double getUpperBound() {
        if(massPoints.isEmpty()) {
            throw new IllegalStateException("no mass points");
        }
        return massPoints.keySet()
                .stream()
                .mapToDouble(value -> value)
                .max()
                .getAsDouble();
    }

    private double getTotalWeight() {
        return massPoints.values()
                .stream()
                .mapToDouble(value -> value)
                .sum();
    }

    @Override
    public double drawValue() {
        if(massPoints.isEmpty()) {
            throw new IllegalStateException("no mass points");
        }
        final double rndValue = rnd.nextDouble() * getTotalWeight();
        double temp = 0.0;
        Double lastValue = null;
        for(Map.Entry<Double, Double> entry: massPoints.entrySet()) {
            temp += entry.getValue();
            lastValue = entry.getKey();
            if(rndValue < temp) {
                return lastValue;
            }
        }
        //safety check, darf nie vorkommen (passiert nur, wenn key null war)
        if(lastValue == null) {
            throw new IllegalStateException();
        }
        return lastValue;
    }
}
