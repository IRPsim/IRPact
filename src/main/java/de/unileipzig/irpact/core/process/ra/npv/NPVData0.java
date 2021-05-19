package de.unileipzig.irpact.core.process.ra.npv;

/**
 * @author Daniel Abitz
 */
public interface NPVData0 {

    double NPV(int N, int A);

    double totalNPV();

    double averageNPV();
}
