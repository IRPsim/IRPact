package de.unileipzig.irpact.commons;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class NameableBase implements Nameable {

    protected String name;

    public NameableBase() {
    }

    public NameableBase(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasName(String input) {
        return Objects.equals(name, input);
    }
}
