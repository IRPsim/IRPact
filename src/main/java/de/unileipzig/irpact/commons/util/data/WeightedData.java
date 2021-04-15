package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class WeightedData<T> {

    protected Map<T, Weight> entries;
    protected boolean normalized = false;
    protected boolean autoNormalize = true;

    public WeightedData() {
        this(new LinkedHashMap<>());
    }

    public WeightedData(Map<T, Weight> entries) {
        this.entries = entries;
    }

    protected void changed() {
        normalized = false;
    }

    protected Weight computeIfAbsent(T key) {
        return entries.computeIfAbsent(key, _key -> new Weight(0));
    }

    public void setAutoNormalize(boolean autoNormalize) {
        this.autoNormalize = autoNormalize;
    }

    public boolean isAutoNormalize() {
        return autoNormalize;
    }

    public Collection<T> keys() {
        return entries.keySet();
    }

    public void set(T key, double weight) {
        Weight w = computeIfAbsent(key);
        w.setWeight(weight);
        changed();
    }

    public void update(T key, double delta) {
        Weight w = computeIfAbsent(key);
        w.updateWeight(delta);
        changed();
    }

    public double get(T key) throws NoSuchElementException {
        Weight w = entries.get(key);
        if(w == null) {
            throw new NoSuchElementException();
        }
        return w.getWeight();
    }

    public boolean remove(T key) {
        if(entries.remove(key) != null) {
            changed();
            return true;
        } else {
            return false;
        }
    }

    protected double sumWeights() {
        return entries.values()
                .stream()
                .mapToDouble(Weight::getWeight)
                .sum();
    }

    public boolean isNormalized() {
        return normalized;
    }

    public boolean isNotNormalized() {
        return !isNormalized();
    }

    public void normalize() {
        if(isNotNormalized()) {
            double sum = sumWeights();
            for(Weight w: entries.values()) {
                w.normalize(sum);
            }
            normalized = true;
        }
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    protected T draw(Rnd rnd, double sum, boolean normalized) {
        final double rndDraw = normalized
                ? rnd.nextDouble()
                : rnd.nextDouble(sum);
        double temp = 0.0;
        T draw = null;
        for(Map.Entry<T, Weight> entry: entries.entrySet()) {
            temp += normalized
                    ? entry.getValue().getNormalized()
                    : entry.getValue().getWeight();
            draw = entry.getKey();
            if(rndDraw < temp) {
                return draw;
            }
        }
        return draw;
    }

    protected T normalizedDraw(Rnd rnd) {
        return draw(rnd, 1.0, true);
    }

    protected T notNormalizedDraw(Rnd rnd) {
        return draw(rnd, sumWeights(), false);
    }

    public T draw(Rnd rnd) {
        if(isEmpty()) {
            throw new IllegalStateException("empty data");
        }

        if(isNormalized()) {
            return normalizedDraw(rnd);
        } else {
            if(autoNormalize) {
                normalize();
                return normalizedDraw(rnd);
            } else {
                return notNormalizedDraw(rnd);
            }
        }
    }

    /**
     * @author Daniel Abitz
     */
    protected static final class Weight {

        protected double weight;
        protected double normalized;

        protected Weight(double weight) {
            this.weight = weight;
        }

        protected void setWeight(double weight) {
            this.weight = weight;
        }

        protected void updateWeight(double delta) {
            weight += delta;
        }

        protected double getWeight() {
            return weight;
        }

        protected void normalize(double sum) {
            normalized = weight / sum;
        }

        protected double getNormalized() {
            return normalized;
        }
    }
}
