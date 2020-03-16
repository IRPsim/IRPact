package de.unileipzig.irpact.core.preference;

import java.util.Comparator;

/**
 * @author Daniel Abitz
 * @implNote Comparable mit aenderbarer Preference-Staerke ist nicht empfehlenswert (designtechnisch).
 *           Daher lieber ein Comparator.
 */
public final class PreferenceStrengthComparator implements Comparator<Preference> {

    public static final PreferenceStrengthComparator INSTANCE = new PreferenceStrengthComparator();

    @Override
    public int compare(Preference o1, Preference o2) {
        return Double.compare(o1.getStrength(), o2.getStrength());
    }
}
