package de.unileipzig.irpact.core.affinity;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class BasicAffinities<T> implements Affinities<T> {

    protected Map<T, Double> values;

    public BasicAffinities(Map<T, Double> values) {
        this.values = values;
    }

    public void putValue(T target, double value) {
        values.put(target, value);
    }

    public void removeValue(T target) {
        values.remove(target);
    }

    @Override
    public boolean hasValue(T target) {
        return values.containsKey(target);
    }

    @Override
    public double getValue(T target) {
        Double value = values.get(target);
        if(value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public void setValue(T target, double value) {
        values.put(target, value);
    }

    private double sum() {
        return values.values()
                .stream()
                .mapToDouble(v -> v)
                .sum();
    }

    @Override
    public T getRandom(Random rnd) {
        final double sum = sum();
        double temp = 0.0;
        final double rndDraw = rnd.nextDouble() * sum;
        T draw = null;
        for(Map.Entry<T, Double> entry: values.entrySet()) {
            temp += entry.getValue();
            draw = entry.getKey();
            if(rndDraw < temp) {
                return draw;
            }
        }
        //safety check, darf nie vorkommen (passiert nur, wenn key null war)
        if(draw == null) {
            throw new IllegalStateException();
        }
        return draw;
    }
}
