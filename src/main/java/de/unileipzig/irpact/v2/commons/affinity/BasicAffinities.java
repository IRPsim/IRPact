package de.unileipzig.irpact.v2.commons.affinity;

import de.unileipzig.irpact.v2.commons.CollectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class BasicAffinities<T> implements Affinities<T> {

    protected Map<T, Double> values;

    public BasicAffinities() {
        this(new HashMap<>());
    }

    public BasicAffinities(Map<T, Double> values) {
        this.values = values;
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
    public T getRandom(Random rnd) {
        return values.isEmpty()
                ? null
                : CollectionUtil.getRandom(values.keySet(), rnd);
    }

    @Override
    public T getWeightedRandom(Random rnd) {
        final double sum = sum();
        final double rndDraw = rnd.nextDouble() * sum;
        double temp = 0.0;
        T draw = null;
        for(Map.Entry<T, Double> entry: values.entrySet()) {
            temp += entry.getValue();
            draw = entry.getKey();
            if(rndDraw < temp) {
                return draw;
            }
        }
        return draw;
    }
}
