package de.unileipzig.irpact.temp;

import de.unileipzig.irpact.experimental.todev.time.ContinuousConverter;
import de.unileipzig.irpact.experimental.todev.time.TickConverter;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class TTimerAgentData {

    private String name;
    private AgentGroup[] groups;
    private ContinuousConverter continuousConverter;
    private TickConverter tickConverter;

    public TTimerAgentData(String name, AgentGroup[] groups, ContinuousConverter continuousConverter, TickConverter tickConverter) {
        this.name = name;
        this.groups = groups;
        this.continuousConverter = continuousConverter;
        this.tickConverter = tickConverter;
    }

    public String getName() {
        return name;
    }

    public AgentGroup[] getGroups() {
        return groups;
    }

    public ContinuousConverter getContinuousConverter() {
        return continuousConverter;
    }

    public TickConverter getTickConverter() {
        return tickConverter;
    }
}
