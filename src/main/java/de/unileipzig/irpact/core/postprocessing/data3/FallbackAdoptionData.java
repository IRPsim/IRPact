package de.unileipzig.irpact.core.postprocessing.data3;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class FallbackAdoptionData implements RealAdoptionData {

    protected int value;

    public FallbackAdoptionData(int value) {
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
    public boolean hasZip(String zip) {
        return true;
    }

    @Override
    public void getValidZips(Collection<? extends String> input, Collection<? super String> output) {
        output.addAll(input);
    }

    @Override
    public void getInvalidZips(Collection<? extends String> input, Collection<? super String> output) {
    }

    @Override
    public void getUnusedZips(Collection<? extends String> input, Collection<? super String> output) {
    }

    @Override
    public int getCumulated(int year, String zip) {
        return value;
    }

    @Override
    public int getUncumulated(int year, String zip) {
        return value;
    }

    @Override
    public int getCumulated(int year, Collection<? extends String> zips) {
        return value;
    }

    @Override
    public int getUncumulated(int year, Collection<? extends String> zips) {
        return value;
    }

    @Override
    public Set<String> getAllZips() {
        return Collections.emptySet();
    }
}
