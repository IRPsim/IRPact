package de.unileipzig.irpact.experimental.tests.reference;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class RefData<T> {

    protected T value;

    public RefData(T value) {
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
        return "RefData{" +
                "value=" + value +
                '}';
    }
}
