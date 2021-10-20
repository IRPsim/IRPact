package de.unileipzig.irpact.core.postprocessing.data3;

import java.util.Collection;
import java.util.Set;

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

    void getValidZips(Collection<? extends String> input, Collection<? super String> output);

    void getInvalidZips(Collection<? extends String> input, Collection<? super String> output);

    void getUnusedZips(Collection<? extends String> input, Collection<? super String> output);

    int getCumulated(int year, String zip);

    int getUncumulated(int year, String zip);

    int getCumulated(int year, Collection<? extends String> zips);

    int getUncumulated(int year, Collection<? extends String> zips);

    int getCumulated(int year);

    int getUncumulated(int year);
}
