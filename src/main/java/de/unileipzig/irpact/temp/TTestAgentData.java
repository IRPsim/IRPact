package de.unileipzig.irpact.temp;

import de.unileipzig.irpact.experimental.todev.time.ContinuousConverter;
import de.unileipzig.irpact.experimental.todev.time.TickConverter;
import de.unileipzig.irpact.start.irpact.input.IRPactInputData;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class TTestAgentData {

    private IRPactInputData data;
    private String name;
    private ContinuousConverter continuousConverter;
    private TickConverter tickConverter;

    public TTestAgentData(IRPactInputData data, String name, ContinuousConverter continuousConverter, TickConverter tickConverter) {
        this.data = data;
        this.name = name;
        this.continuousConverter = continuousConverter;
        this.tickConverter = tickConverter;
    }

    public IRPactInputData getData() {
        return data;
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
