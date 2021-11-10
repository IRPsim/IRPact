package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class FixValues<T> implements Iterator<T> {

    protected int index = 0;
    protected List<T> values = new ArrayList<>();

    public FixValues() {
    }

    @SuppressWarnings("unchecked")
    public FixValues(T... values) {
        addAll(values);
    }

    public void add(T value) {
        values.add(value);
    }

    @SuppressWarnings("unchecked")
    public void addAll(T... values) {
        Collections.addAll(this.values, values);
    }

    public void reset() {
        index = 0;
    }

    public int remaining() {
        return values.size() - index;
    }

    @Override
    public boolean hasNext() {
        return index < values.size();
    }

    @Override
    public T next() {
        if(hasNext()) {
            return values.get(index++);
        } else {
            throw new NoSuchElementException();
        }
    }
}
