package de.unileipzig.irpact.commons;

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
}
