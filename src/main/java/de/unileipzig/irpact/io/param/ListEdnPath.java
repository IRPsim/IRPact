package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.util.ListSupplier;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public final class ListEdnPath implements EdnPath {

    protected ListSupplier supplier;
    protected List<String> path;

    public ListEdnPath(String first) {
        this(ListSupplier.ARRAY, first);
    }

    public ListEdnPath(ListSupplier supplier, String first) {
        this.supplier = supplier;
        this.path = supplier.newList();
        path.add(first);
    }

    protected ListEdnPath(ListEdnPath other) {
        this.supplier = other.supplier;
        this.path = other.supplier.newList();
        this.path.addAll(other.path);
    }

    @Override
    public ListEdnPath copy() {
        return new ListEdnPath(this);
    }

    @Override
    public ListEdnPath resolve(String next) {
        return copy().add(next);
    }

    @Override
    public ListEdnPath resolveSibling(String next) {
        return copy().replaceLast(next);
    }

    @Override
    public EdnPath addTo(Collection<? super EdnPath> target) {
        target.add(this);
        return this;
    }

    public ListEdnPath add(String next) {
        path.add(next);
        return this;
    }

    public ListEdnPath replaceLast(String newNext) {
        if(isEmpty()) {
            throw new NoSuchElementException();
        } else {
            path.set(lastIndex(), newNext);
            return this;
        }
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    @Override
    public int length() {
        return path.size();
    }

    public int lastIndex() {
        return length() - 1;
    }

    public int secondToLastIndex() {
        return length() - 2;
    }

    @Override
    public String get(int index) {
        return path.get(index);
    }

    @Override
    public String getSecondToLast() {
        if(length() < 2) {
            throw new NoSuchElementException();
        } else {
            return get(secondToLastIndex());
        }
    }

    @Override
    public String getLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return get(lastIndex());
        }
    }

    @Override
    public Iterator<String> iterator() {
        return path.iterator();
    }

    @Override
    public String[] toArray() {
        return path.toArray(new String[0]);
    }

    @Override
    public String[] toArrayWithoutRoot() {
        if(length() < 2) {
            throw new IllegalArgumentException("not enough parts");
        }

        String[] arr = new String[length() - 1];
        for(int i = 1; i < length(); i++) {
            arr[i-1] = get(i);
        }
        return arr;
    }

    @Override
    public Stream<String> stream() {
        return path.stream();
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
