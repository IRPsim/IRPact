package de.unileipzig.irpact.temp;

import de.unileipzig.irpact.experimental.todev.time.ContinuousConverter;
import de.unileipzig.irpact.experimental.todev.time.TickConverter;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class TTimerAgentData {

    private String name;
    private ContinuousConverter continuousConverter;
    private TickConverter tickConverter;

    public TTimerAgentData(String name, ContinuousConverter continuousConverter, TickConverter tickConverter) {
        this.name = name;
        this.continuousConverter = continuousConverter;
        this.tickConverter = tickConverter;
    }

    public String getName() {
        return name;
    }

    public ContinuousConverter getContinuousConverter() {
        return continuousConverter;
    }

    public TickConverter getTickConverter() {
        return tickConverter;
    }
}
