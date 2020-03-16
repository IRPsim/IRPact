package de.unileipzig.irpact.core.simulation;

import jdk.nashorn.internal.ir.annotations.Reference;

@Reference
public final class Timestamp implements Comparable<Timestamp> {

    public enum Mode {
        SYSTEM,
        SIMULATION,
        TICK
    }

    private Mode mode;
    private long systemTime;
    private long simulationTime;
    private double tick;

    public Timestamp(
            Mode mode,
            long systemTime,
            long simulationTime,
            double tick) {
        this.mode = mode;
        this.systemTime = systemTime;
        this.simulationTime = simulationTime;
        this.tick = tick;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public long getSimulationTime() {
        return simulationTime;
    }

    public double getTick() {
        return tick;
    }

    @Override
    public int compareTo(Timestamp other) {
        switch (mode) {
            case SYSTEM:
                return Long.compare(systemTime, other.systemTime);
            case SIMULATION:
                return Long.compare(simulationTime, other.simulationTime);
            case TICK:
                return Double.compare(tick, other.tick);
        }
        throw new IllegalStateException("impossible switch case");
    }

    @Override
    public String toString() {
        switch (mode) {
            case SYSTEM:
                return "Timestamp[" + systemTime + "]";
            case SIMULATION:
                return "Timestamp[" + simulationTime + "]";
            case TICK:
                return "Timestamp[" + tick + "]";
        }
        throw new IllegalStateException("impossible switch case");
    }
}
