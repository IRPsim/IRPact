package de.unileipzig.irpact.core.postprocessing.data3;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public interface RealAdoptionData {

    void createScaledAdoptionData(double scale, boolean fixError);

    boolean hasScaledAdoptionData(double scale);

    ScaledRealAdoptionData getScaledAdoptionData(double scale);

    Set<String> getAllZips();

    Set<Integer> getAllYears();

    boolean hasZip(String zip);

    boolean hasYear(int year);

    void getValidZips(Collection<? extends String> input, Collection<? super String> output);

    default List<String> listValidZips(Collection<? extends String> input) {
        Set<String> valid = new LinkedHashSet<>();
        getValidZips(input, valid);
        return new ArrayList<>(valid);
    }

    void getInvalidZips(Collection<? extends String> input, Collection<? super String> output);

    default List<String> listInvalidZips(Collection<? extends String> input) {
        Set<String> invalid = new LinkedHashSet<>();
        getInvalidZips(input, invalid);
        return new ArrayList<>(invalid);
    }

    void getUnusedZips(Collection<? extends String> input, Collection<? super String> output);

    default List<String> listUnusedZips(Collection<? extends String> input) {
        Set<String> unused = new LinkedHashSet<>();
        getUnusedZips(input, unused);
        return new ArrayList<>(unused);
    }

    int getCumulated(int year, String zip);

    int getUncumulated(int year, String zip);

    int getCumulated(int year, Collection<? extends String> zips);

    int getUncumulated(int year, Collection<? extends String> zips);

    int getCumulated(int year);

    int getUncumulated(int year);
}
