package de.unileipzig.irpact.v2.core.time;

import de.unileipzig.irpact.v2.commons.time.TimeMode;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractTimeModel implements TimeModel {

    protected TimeMode mode;

    public AbstractTimeModel() {
    }

    public void setMode(TimeMode mode) {
        this.mode = mode;
    }

    @Override
    public TimeMode getMode() {
        return mode;
    }
}
