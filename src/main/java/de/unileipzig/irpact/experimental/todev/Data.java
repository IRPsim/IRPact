package de.unileipzig.irpact.experimental.todev;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;

import java.util.NoSuchElementException;

/**
 * Container fuer Daten. Erlaubt die Nutzung von null als validen Wert.
 *
 * @param <T> Unterschiedliche Graphtypen
 * @author Daniel Abitz
 */
@ToDevelop
public final class Data<T> {

    private boolean has = false;
    private T data;

    public Data() {
    }

    public Data(T data) {
        set(data);
    }

    public static <T> Data<T> of() {
        return new Data<>();
    }

    public static <T> Data<T> of(T value) {
        return new Data<>(value);
    }

    public void set(T data) {
        this.data = data;
        has = true;
    }

    public void clear() {
        data = null;
        has = false;
    }

    public T get() throws NoSuchElementException {
        if(has) {
            return data;
        }
        throw new NoSuchElementException();
    }

    public T getOr(T other) {
        return has ? data : other;
    }

    public boolean has() {
        return has;
    }

    public boolean isEmpty() {
        return !has;
    }

    @Override
    public String toString() {
        return "Data{" +
                "has=" + has +
                ", data=" + data +
                '}';
    }
}
