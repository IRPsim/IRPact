package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class FallbackAdoptionData implements ScaledRealAdoptionData {

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
    public boolean hasYear(int year) {
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
    public String printCumulatedData() {
        return Integer.toString(value);
    }

    @Override
    public String printUncumulatedData() {
        return Integer.toString(value);
    }

    @Override
    public void createScaledAdoptionData(double scale, boolean fixError) {
    }

    @Override
    public boolean hasScaledAdoptionData(double scale) {
        return true;
    }

    @Override
    public ScaledRealAdoptionData getScaledAdoptionData(double scale) {
        return this;
    }

    @Override
    public Set<String> getAllZips() {
        return Collections.emptySet();
    }

    @Override
    public int getCumulated(int year) {
        return value;
    }

    @Override
    public int getUncumulated(int year) {
        return value;
    }

    @Override
    public double getScale() {
        return Double.NaN;
    }
}
