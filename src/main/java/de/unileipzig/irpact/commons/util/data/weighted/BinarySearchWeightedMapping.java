package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <T>
 * @author Daniel Abitz
 */
public class BinarySearchWeightedMapping<T> implements WeightedMapping<T> {

    protected List<WeightedValue<T>> elements;
    protected List<T> rawElements;
    protected double[] ascWeightArray;
    protected boolean sorted = false;
    protected boolean normalized = false;
    protected boolean autoNormalize = false;
    protected boolean disableWeights = false;

    public BinarySearchWeightedMapping() {
        this(new ArrayList<>());
    }

    public BinarySearchWeightedMapping(List<WeightedValue<T>> elements) {
        this.elements = elements;
        rawElements = elements.stream()
                .map(WeightedValue::getValue)
                .collect(Collectors.toList());
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

    @Override
    public BinarySearchWeightedMapping<T> copy() {
        List<WeightedValue<T>> copy = new ArrayList<>(elements);
        return createCopy(copy);
    }

    @Override
    public BinarySearchWeightedMapping<T> copyWithout(T toRemove) {
        List<WeightedValue<T>> copy = new ArrayList<>(elements);
        int index = indexOf(toRemove);
        if(index != -1) {
            copy.remove(index);
        }
        return createCopy(copy);
    }

    protected BinarySearchWeightedMapping<T> createCopy(List<WeightedValue<T>> copy) {
        BinarySearchWeightedMapping<T> copyMapping = new BinarySearchWeightedMapping<>(copy);
        copyMapping.setDisableWeights(isDisableWeights());
        copyMapping.setAutoNormalize(isAutoNormalize());
        return copyMapping;
    }

    @Override
    public Collection<T> elements() {
        return rawElements;
    }

    @Override
    public double totalWeight() {
        return elements.stream()
                .mapToDouble(WeightedValue::getWeight)
                .sum();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean allowsZeroWeight() {
        return false;
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
        rawElements.remove(index);
        return true;
    }

    protected static void nonZeroWeight(double weight) {
        if(weight == 0.0) {
            throw new IllegalArgumentException("weight is zero");
        }
    }

    @Override
    public void set(T target, double weight) {
        set(new WeightedValue<>(target, weight));
    }

    @Override
    public void set(WeightedValue<T> value) {
        nonZeroWeight(value.getWeight());
        int index = indexOf(value.getValue());
        if(index == -1) {
            elements.add(value);
            rawElements.add(value.getValue());
        } else {
            elements.set(index, value);
            rawElements.set(index, value.getValue());
        }
        normalized = false;
        sorted = false;
    }

    public void add(T target, double weight) {
        add(new WeightedValue<>(target, weight));
    }

    public void add(WeightedValue<T> value) {
        nonZeroWeight(value.getWeight());
        elements.add(value);
        rawElements.add(value.getValue());
        normalized = false;
        sorted = false;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    protected void checkNotEmpty() {
        if(isEmpty()) {
            throw new IllegalStateException("empty");
        }
    }

    @Override
    public T getRandom(Rnd rnd) {
        checkNotEmpty();
        if(size() == 1) {
            return elements.get(0).getValue();
        }
        return rnd.getRandom(elements).getValue();
    }

    protected void sort() {
        elements.sort(WeightedValue.getAscendingWeightComparator());
        ascWeightArray = new double[elements.size()];
        double lastWeight = 0;
        for(int i = 0; i < elements.size(); i++) {
            ascWeightArray[i] = elements.get(i).getWeight() + lastWeight;
            lastWeight = ascWeightArray[i];
            rawElements.set(i, elements.get(i).getValue());
        }
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        if(disableWeights) {
            return getRandom(rnd);
        }
        checkNotEmpty();
        tryNormalize();
        if(!sorted) {
            sort();
            sorted = true;
        }
        if(size() == 1) {
            return elements.get(0).getValue();
        }
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
