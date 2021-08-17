package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BinaryAwareness<T, U> implements Awareness<T> {

    protected Set<T> items;

    public BinaryAwareness() {
        this(new LinkedHashSet<>());
    }

    public BinaryAwareness(Set<T> items) {
        this.items = items;
    }

    public Set<T> getItems() {
        return items;
    }

    @Override
    public boolean isAware(T item) {
        return items.contains(item);
    }

    @Override
    public void makeAware(T item) {
        items.add(item);
    }

    @Override
    public void forget(T item) {
        items.remove(item);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.unsupportedChecksum(getClass());
    }
}
