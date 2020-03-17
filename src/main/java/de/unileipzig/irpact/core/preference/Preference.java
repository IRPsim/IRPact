package de.unileipzig.irpact.core.preference;

/**
 * @author Daniel Abitz
 */
public interface Preference {

    Value getValue();

    double getStrength();

    void setStrength(double newStrength);
}
