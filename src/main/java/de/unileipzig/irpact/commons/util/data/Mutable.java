package de.unileipzig.irpact.commons.util.data;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Mutable<T> {

    private boolean hasValue;
    private T value;

    public Mutable() {
        clear();
    }

    public Mutable(T value) {
        set(value);
    }

    public static <T> Mutable<T> empty() {
        return new Mutable<>();
    }

    public static <T> Mutable<T> wrap(T value) {
        return new Mutable<>(value);
    }

    protected void requiresValue() {
        if(!hasValue) {
            throw new IllegalStateException("no value");
        }
    }

    public boolean hasValue() {
        return hasValue;
    }

    public boolean isEmpty() {
        return !hasValue;
    }

    public void clear() {
        value = null;
        hasValue = false;
    }

    public void set(T value) {
        this.value = value;
        hasValue = true;
    }

    public T get() {
        requiresValue();
        return value;
    }

    public T getAndSet(T newValue) {
        T oldValue = get();
        set(newValue);
        return oldValue;
    }

    @Override
    public String toString() {
        return hasValue()
                ? "Mutable(" + value + ")"
                : "Mutable.empty";
    }
}
