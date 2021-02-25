package de.unileipzig.irpact.core.simulation;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicVersion implements Version {

    private String text;

    public BasicVersion() {
    }

    public BasicVersion(String text) {
        this.text = text;
    }

    public void set(String text) {
        this.text = text;
    }

    @Override
    public boolean isMismatch(Version other) {
        return !Objects.equals(print(), other.print());
    }

    @Override
    public String print() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicVersion)) return false;
        BasicVersion that = (BasicVersion) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
