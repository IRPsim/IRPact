package de.unileipzig.irpact.v2.jadex.time;

import de.unileipzig.irpact.v2.core.time.AbstractTimeModel;
import jadex.bridge.service.types.clock.IClockService;

/**
 * @author Daniel Abitz
 */
public abstract class JadexTimeModel extends AbstractTimeModel {

    protected IClockService clock;

    public JadexTimeModel() {
    }

    public void setClock(IClockService clock) {
        this.clock = clock;
    }

    public IClockService getClock() {
        return clock;
    }
}
