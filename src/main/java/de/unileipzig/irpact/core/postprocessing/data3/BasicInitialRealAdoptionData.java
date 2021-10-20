package de.unileipzig.irpact.core.postprocessing.data3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicInitialRealAdoptionData implements InitialRealAdoptionData {

    protected int year;
    protected double scale;
    protected Map<String, Integer> zipBasedAdoptions;

    public BasicInitialRealAdoptionData() {
        this(new HashMap<>());
    }

    public BasicInitialRealAdoptionData(int year, double scale) {
        this();
        setYear(year);
        setScale(scale);
    }

    public BasicInitialRealAdoptionData(Map<String, Integer> zipBasedAdoptions) {
        this.zipBasedAdoptions = zipBasedAdoptions;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int getYear() {
        return year;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public double getScale() {
        return scale;
    }

    public void put(String zip, int adoptions) {
        zipBasedAdoptions.put(zip, adoptions);
    }

    public Map<String, Integer> getZipBasedAdoptions() {
        return zipBasedAdoptions;
    }

    @Override
    public Set<String> getZips() {
        return zipBasedAdoptions.keySet();
    }

    @Override
    public int getAdoptions(String zip) {
        Integer adoptions = zipBasedAdoptions.get(zip);
        return adoptions == null
                ? 0
                : Math.max(0, adoptions);
    }

    @Override
    public int getAdoptions(Collection<? extends String> zips) {
        int total = 0;
        for(String zip: zips) {
            total += getAdoptions(zip);
        }
        return total;
    }
}
