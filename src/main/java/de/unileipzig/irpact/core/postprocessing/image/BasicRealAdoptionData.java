package de.unileipzig.irpact.core.postprocessing.image;

import java.util.Collections;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicRealAdoptionData implements RealAdoptionData {

    protected int value;

    public BasicRealAdoptionData(int value) {
        setValue(value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Set<Integer> getAllYears() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getAllZips() {
        return Collections.emptySet();
    }

    @Override
    public int get(int year, String zip) {
        return value;
    }
}
