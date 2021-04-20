package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class BasicAffinities<T> implements Affinities<T> {

    protected Map<T, Double> values;

    public BasicAffinities() {
        this(new LinkedHashMap<>());
    }

    public BasicAffinities(Map<T, Double> values) {
        this.values = values;
    }

    protected BasicAffinities<T> newInstance() {
        return new BasicAffinities<>();
    }

    @Override
    public Affinities<T> createWithout(T target) {
        BasicAffinities<T> copy = newInstance();
        copy.values.putAll(values);
        copy.values.remove(target);
        return copy;
    }

    @Override
    public Set<T> targets() {
        return values.keySet();
    }

    @Override
    public Set<T> accessibleTargets() {
        return values.entrySet().stream()
                .filter(e -> e.getValue() > 0.0)
                .map(Map.Entry::getKey)
                .collect(CollectionUtil.collectToLinkedSet());
    }

    @Override
    public boolean isEmpty() {
        if(values.isEmpty()) {
            return true;
        } else {
            long numberOfZeroWeight = values.values()
                    .stream()
                    .mapToDouble(d -> d)
                    .filter(d -> d == 0.0)
                    .count();
            return numberOfZeroWeight == values.size();
        }
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean hasValue(T target) {
        return values.containsKey(target);
    }

    @Override
    public double getValue(T target) {
        Double v = values.get(target);
        if(v == null) {
            throw new NoSuchElementException();
        }
        return v;
    }

    @Override
    public void setValue(T target, double value) {
        values.put(target, value);
    }

    @Override
    public double sum() {
        return values.values()
                .stream()
                .mapToDouble(v -> v)
                .sum();
    }

    @Override
    public T getRandom(Rnd rnd) {
        return values.isEmpty()
                ? null
                : CollectionUtil.getRandom(values.keySet(), rnd);
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        final double sum = sum();
        final double rndDraw = rnd.nextDouble() * sum;
        double temp = 0.0;
        T draw = null;
        boolean drawn = false;
        for(Map.Entry<T, Double> entry: values.entrySet()) {
            double weight = entry.getValue();
            if(weight == 0.0) {
                continue;
            }
            temp += weight;
            draw = entry.getKey();
            drawn = true;
            if(rndDraw < temp) {
                return draw;
            }
        }
        if(!drawn) {
            throw new NoSuchElementException();
        }
        return draw;
    }
}
