package de.unileipzig.irpact.v2.commons.distribution;

import de.unileipzig.irpact.v2.commons.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class FiniteMassPointsDiscreteDistribution extends AbstractBoundedUnivariateDoubleDistribution {

    protected long seed;
    protected Random rnd;
    protected List<Pair<Double, Double>> massPoints;

    public FiniteMassPointsDiscreteDistribution() {
        this(new ArrayList<>());
    }

    public FiniteMassPointsDiscreteDistribution(List<Pair<Double, Double>> massPoints) {
        this.massPoints = massPoints;
    }

    public void init(long seed) {
        setSeed(seed);
        setRandom(new Random(seed));
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    public Random getRandom() {
        return rnd;
    }

    public void init(List<Pair<Double, Double>> input) {
        if(input.isEmpty()) {
            throw new IllegalStateException("empty");
        }
        massPoints.clear();
        double sum = input.stream()
                .mapToDouble(Pair::second)
                .sum();
        upperBound = Double.MIN_VALUE;
        lowerBound = Double.MAX_VALUE;
        for(Pair<Double, Double> pair: input) {
            double value = pair.first();
            double weight = pair.second();
            massPoints.add(new Pair<>(value, weight / sum)); //normalize
            if(value > upperBound) {
                upperBound = value;
            }
            if(value < lowerBound) {
                lowerBound = value;
            }
        }
        massPoints.sort(Comparator.comparingDouble(Pair::second));
    }

    @Override
    public double drawDoubleValue() {
        if(massPoints.isEmpty()) {
            throw new IllegalStateException("empty");
        }
        final double rndValue = rnd.nextDouble();
        double temp = 0.0;
        double lastValue = 0.0;
        for(Pair<Double, Double> massPoint: massPoints) {
            temp += massPoint.second();
            lastValue = massPoint.first();
            if(rndValue < temp) {
                return lastValue;
            }
        }
        return lastValue;
    }
}
