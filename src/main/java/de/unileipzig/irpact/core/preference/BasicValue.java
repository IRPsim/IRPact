package de.unileipzig.irpact.core.preference;

/**
 * @author Daniel Abitz
 */
public class BasicValue implements Value {

    private String name;

    public BasicValue(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
