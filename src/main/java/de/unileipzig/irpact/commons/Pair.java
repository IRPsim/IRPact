package de.unileipzig.irpact.commons;

import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Pair<A, B> implements Map.Entry<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Pair<A, B> get(A first, B second) {
        return new Pair<>(first, second);
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public A getKey() {
        return first();
    }

    @Override
    public B getValue() {
        return second();
    }

    @Override
    public B setValue(B value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Pair) {
            Pair<?, ?> other = (Pair<?, ?>) obj;
            return Objects.equals(first, other.first)
                    && Objects.equals(second, other.second);
        }
        return false;
    }
}
