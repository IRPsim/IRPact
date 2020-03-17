package de.unileipzig.irpact.core.preference;

/**
 * @author Daniel Abitz
 */
public final class BasicPreference implements Preference {

    private Value value;
    private double strength;

    public BasicPreference(Value value, double strength) {
        this.value = value;
        this.strength = strength;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public double getStrength() {
        return strength;
    }

    @Override
    public void setStrength(double strength) {
        this.strength = strength;
    }
}
