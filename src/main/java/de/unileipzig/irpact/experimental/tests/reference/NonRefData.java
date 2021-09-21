package de.unileipzig.irpact.experimental.tests.reference;

/**
 * @author Daniel Abitz
 */
public class NonRefData<T> {

    protected T value;

    public NonRefData(T value) {
        set(value);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NonRefData{" +
                "value=" + value +
                '}';
    }
}
