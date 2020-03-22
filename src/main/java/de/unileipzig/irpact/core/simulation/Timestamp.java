package de.unileipzig.irpact.core.simulation;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class Timestamp implements Comparable<Timestamp> {

    private final TimeModule.Mode mode;
    private final long systemTime;
    private final long simulationTime;
    private final double tick;

    public Timestamp(
            TimeModule.Mode mode,
            long systemTime,
            long simulationTime,
            double tick) {
        this.mode = mode;
        this.systemTime = systemTime;
        this.simulationTime = simulationTime;
        this.tick = tick;
    }

    public TimeModule.Mode getMode() {
        return mode;
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
            default:
                throw new IllegalStateException("impossible switch case");
        }
    }

    @Override
    public int hashCode() {
        switch (mode) {
            case SYSTEM:
                return Objects.hash(mode, systemTime);
            case SIMULATION:
                return Objects.hash(mode, simulationTime);
            case TICK:
                return Objects.hash(mode, tick);
            default:
                throw new IllegalStateException("impossible switch case");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Timestamp) {
            Timestamp other = (Timestamp) obj;
            if(mode == other.mode) {
                switch (mode) {
                    case SYSTEM:
                        return systemTime == other.systemTime;
                    case SIMULATION:
                        return simulationTime == other.simulationTime;
                    case TICK:
                        return tick == other.tick;
                    default:
                        throw new IllegalStateException("impossible switch case");
                }
            } else {
                return false;
            }
        }
        return false;
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
            default:
                throw new IllegalStateException("impossible switch case");
        }
    }
}
