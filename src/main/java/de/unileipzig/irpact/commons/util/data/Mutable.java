package de.unileipzig.irpact.commons.util.data;

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
                ? "Ref{" + value + "}"
                : "Ref.empty";
    }
}
