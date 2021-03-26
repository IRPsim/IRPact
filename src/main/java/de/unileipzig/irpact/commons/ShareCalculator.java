package de.unileipzig.irpact.commons;

import java.util.*;

/**
 * Helps calculating and managing shares.
 *
 * @author Daniel Abitz
 */
public class ShareCalculator<T> {

    protected Map<T, Integer> individualSizes = new LinkedHashMap<>();
    protected Map<T, Double> individualShares = new LinkedHashMap<>();

    public ShareCalculator() {
    }

    public void reset() {
        individualSizes.clear();
        individualShares.clear();
    }

    public boolean hasSize(T key) {
        return individualSizes.containsKey(key);
    }

    public int getSize(T key) {
        Integer size = individualSizes.get(key);
        if(size == null) {
            throw new NoSuchElementException("key '" + key + "' not found");
        }
        return size;
    }

    public int sumSizes(Collection<? extends T> keys) {
        int totalSize = 0;
        for(T key: keys) {
            totalSize += getSize(key);
        }
        return totalSize;
    }

    public Integer replaceSize(T key, int newSize) {
        return individualSizes.put(key, newSize);
    }

    public void setSize(T key, int size) throws IllegalArgumentException {
        if(hasSize(key)) {
            throw new IllegalArgumentException("key '" + key + "' already exists");
        }
        individualSizes.put(key, size);
    }

    public void calculateShares(int totalSize) {
        individualShares.clear();
        for(Map.Entry<T, Integer> sizeEntry: individualSizes.entrySet()) {
            double share = (double) sizeEntry.getValue() / (double) totalSize;
            individualShares.put(sizeEntry.getKey(), share);
        }
    }

    public double getShare(T key) throws NoSuchElementException {
        Double share = individualShares.get(key);
        if(share == null) {
            throw new NoSuchElementException("key '" + key + "' not found");
        }
        return share;
    }

    public int getProportionalSize(T key, int totalSize) throws NoSuchElementException {
        double share = getShare(key);
        return (int) (share * totalSize);
    }

    public T getKeyWithLargestShare() throws NoSuchElementException {
        Optional<Map.Entry<T, Double>> maxEntry = individualShares.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue));
        if(maxEntry.isPresent()) {
            return maxEntry.get().getKey();
        } else {
            throw new NoSuchElementException();
        }
    }
}
