package de.unileipzig.irpact.core.postprocessing.image;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface RealAdoptionData {

    Set<String> getAllZips();

    Set<Integer> getAllYears();

    int get(int year, String zip);
}
