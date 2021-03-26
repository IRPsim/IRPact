package de.unileipzig.irpact.commons.affinity;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.Rnd;

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
        return values.isEmpty();
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
