package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.simulation.TimeModule;
import de.unileipzig.irpact.core.simulation.Timestamp;
import jadex.bridge.service.types.clock.IClockService;

/**
 * @author Daniel Abitz
 */
public class JadexTimeModule implements TimeModule {

    private Timestamp.Mode mode = Timestamp.Mode.SYSTEM;
    private IClockService clockService;

    public JadexTimeModule(IClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void setTimeMode(Timestamp.Mode mode) {
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
}
