package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.TimeModule;
import de.unileipzig.irpact.core.simulation.Timestamp;
import jadex.bridge.service.types.clock.IClockService;

/**
 * @author Daniel Abitz
 */
public class JadexTimeModule implements TimeModule {

    private Mode mode = Mode.SYSTEM;
    private IClockService clockService;

    public JadexTimeModule(IClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void setTimeMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public long getSimulationStarttime() {
        return clockService.getStarttime();
    }

    @Override
    public long getSimulationTime() {
        return clockService.getTime();
    }

    @Override
    public double getTick() {
        return clockService.getTick();
    }

    @Override
    public long getSystemTime() {
        return System.currentTimeMillis();
    }

    @Override
    public Timestamp createTimestamp() {
        return new Timestamp(
                mode,
                getSystemTime(),
                getSimulationTime(),
                getTick()
        );
    }

    @Override
    public Timestamp createTimestamp(double delta) {
        return new Timestamp(
                mode,
                getSystemTime() + (long) delta,
                getSimulationTime() + (long) delta,
                getTick() + delta
        );
    }

    @Override
    public Timestamp createTimestamp(long delta) {
        return new Timestamp(
                mode,
                getSystemTime() + delta,
                getSimulationTime() + delta,
                getTick() + delta
        );
    }
}
