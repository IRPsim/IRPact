package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.WeightedValue;

import java.util.*;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class WeightedListMapping<T> implements WeightedMapping<T> {

    protected List<WeightedValue<T>> elements;
    protected double[] ascWeightArray;
    protected boolean sorted = false;
    protected boolean normalized = false;
    protected boolean autoNormalize = false;
    protected boolean disableWeights = false;

    public WeightedListMapping() {
        this(new ArrayList<>());
    }

    public WeightedListMapping(List<WeightedValue<T>> elements) {
        this.elements = elements;
    }

    public void setAutoNormalize(boolean autoNormalize) {
        this.autoNormalize = autoNormalize;
    }

    public boolean isAutoNormalize() {
        return autoNormalize;
    }

    public boolean isDisableWeights() {
        return disableWeights;
    }

    public void setDisableWeights(boolean disableWeights) {
        this.disableWeights = disableWeights;
    }

    public void clear() {
        elements.clear();
    }

    public void normalize() {
        if(normalized) {
            return;
        }
        double totalWeight = totalWeight();
        for(int i = 0; i < elements.size(); i++) {
            elements.set(i, elements.get(i).normalize(totalWeight));
        }
        normalized = true;
    }

    protected void tryNormalize() {
        if(autoNormalize) {
            normalize();
        }
    }

    public boolean isNormalized() {
        return normalized;
    }

    protected double totalWeight() {
        return elements.stream()
                .mapToDouble(WeightedValue::getWeight)
                .sum();
    }

    public int size() {
        return elements.size();
    }

    protected int indexOf(T target) {
        for(int i = 0; i < size(); i++) {
            if(elements.get(i).getValue() == target) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean has(T target) {
        return indexOf(target) != -1;
    }

    @Override
    public double getWeight(T target) {
        int index = indexOf(target);
        return index == -1 ? 0 : elements.get(index).getWeight();
    }

    @Override
    public boolean remove(T target) {
        int index = indexOf(target);
        if(index == -1) {
            return false;
        }
        elements.remove(index);
        return true;
    }

    @Override
    public void set(T target, double weight) {
        set(new WeightedValue<>(target, weight));
    }

    @Override
    public void set(WeightedValue<T> value) {
        int index = indexOf(value.getValue());
        if(index == -1) {
            elements.add(value);
        } else {
            elements.set(index, value);
        }
        normalized = false;
        sorted = false;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public T getRandom(Rnd rnd) {
        return rnd.getRandom(elements).getValue();
    }

    protected void sort() {
        elements.sort(WeightedValue.getAscendingWeightComparator());
        ascWeightArray = new double[elements.size()];
        double lastWeight = 0;
        for(int i = 0; i < elements.size(); i++) {
            ascWeightArray[i] = elements.get(i).getWeight() + lastWeight;
            lastWeight = ascWeightArray[i];
        }
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        if(disableWeights) {
            return getRandom(rnd);
        }
        if(!sorted) {
            sort();
            sorted = true;
        }
        tryNormalize();
        return isNormalized()
                ? getNormalizedWeightedRandom(rnd)
                : getNotNormalizedWeightedRandom(rnd);
    }

    protected T getNormalizedWeightedRandom(Rnd rnd) {
        return getNormalizedWeightedRandom(rnd, 1.0);
    }

    protected T getNotNormalizedWeightedRandom(Rnd rnd) {
        double totalWeight = totalWeight();
        return getNormalizedWeightedRandom(rnd, totalWeight);
    }

    protected T getNormalizedWeightedRandom(Rnd rnd, double totalWeight) {
        return getNormalizedWeightedRandom(elements, ascWeightArray, rnd, totalWeight);
    }

    protected static <K> K getNormalizedWeightedRandom(
            List<WeightedValue<K>> sortedElements,
            double[] weightArray,
            Rnd rnd,
            double totalWeight) {
        final double rndDraw = rnd.nextDouble(totalWeight);
        int index = Arrays.binarySearch(weightArray, rndDraw);
        if(index < 0) {
            index = -1*index - 1;
        }
        return sortedElements.get(index).getValue();
    }
}
