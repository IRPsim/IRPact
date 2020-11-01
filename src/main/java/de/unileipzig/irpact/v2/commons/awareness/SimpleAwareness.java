package de.unileipzig.irpact.v2.commons.awareness;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class SimpleAwareness<T> implements Awareness<T> {

    protected Set<T> items;

    public SimpleAwareness() {
        this(new HashSet<>());
    }

    public SimpleAwareness(Set<T> items) {
        this.items = items;
    }

    @Override
    public boolean isAwareOf(T item) {
        return items.contains(item);
    }

    @Override
    public void update(T item, double influence) {
        if(influence == 0.0) {
            items.remove(item);
        } else {
            items.add(item);
        }
    }

    @Override
    public void forget(T item) {
        items.remove(item);
    }
}