package de.unileipzig.irpact.core.preference;

import de.unileipzig.irpact.commons.annotation.ToImpl;

/**
 * @author Daniel Abitz
 */
@ToImpl("in consumer einfuegen")
public interface Preference {

    Value getValue();

    double getStrength();

    void setStrength(double newStrength);
}
