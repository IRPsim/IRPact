package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public interface ProgressCalculator {

    void reset();

    boolean setProgress(int phase, double progress);

    double getProgress();
}
