package de.unileipzig.irpact.core.postprocessing.data4;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public enum PerformanceType {
    RMSD("RMSD"),
    MAE("MAE"),
    FSAPE("FSAPE"),
    GLOBAL_ADOPTION_DELTA("globalAdoptionDelta"),
    ABSOLUT_ANNUAL_ADOPTION_DELTA("absoluteAnnualAdoptionDelta"),
    CUMULATIVE_ANNUAL_ADOPTION_DELTA("cumulativeAnnualAdoptionDelta"),
    ;

    private final String name;

    PerformanceType(String name) {
        this.name = name;
    }

    public String print() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PerformanceType get(String name) {
        for(PerformanceType type: values()) {
            if(Objects.equals(type.name, name)) {
                return type;
            }
        }
        return null;
    }
}
