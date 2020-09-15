package de.unileipzig.irpact.temp.jadex;

import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldCanBeLocal")
@Reference(local = true, remote = true)
public class AgentGroupWrapper {

    private AgentGroup group;

    public AgentGroupWrapper(AgentGroup group) {
        this.group = group;
    }

    public AgentGroup getGroup() {
        return group;
    }
}
