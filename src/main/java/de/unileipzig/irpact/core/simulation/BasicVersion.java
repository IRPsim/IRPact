package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public class BasicVersion implements Version {

    private String text;

    public BasicVersion() {
    }

    public void setVersion(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
