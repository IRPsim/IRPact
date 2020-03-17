package de.unileipzig.irpact.core.simulation;

/**
 * @author Daniel Abitz
 */
public interface TimeModule {

    void setTimeMode(Timestamp.Mode mode);

    long getSimulationStarttime();

    default long getSimulationTimeSinceStart() {
        return getSimulationTime() - getSimulationStarttime();
    }

    long getSimulationTime();

    double getTick();

    long getSystemTime();

    Timestamp getTimestamp();
}
