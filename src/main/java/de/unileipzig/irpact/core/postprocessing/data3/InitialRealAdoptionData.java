package de.unileipzig.irpact.core.postprocessing.data3;

import java.util.Collection;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface InitialRealAdoptionData {

    int getYear();

    double getScale();

    Set<String> getZips();

    int getAdoptions(String zip);

    int getAdoptions(Collection<? extends String> zips);
}
