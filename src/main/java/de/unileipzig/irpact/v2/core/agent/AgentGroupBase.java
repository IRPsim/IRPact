package de.unileipzig.irpact.v2.core.agent;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntityBase;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class AgentGroupBase<T extends Agent> extends SimulationEntityBase implements AgentGroup<T> {

    protected Set<T> agents;

    public void setAgents(Set<T> agents) {
        this.agents = agents;
    }

    @Override
    public Set<T> getAgents() {
        return agents;
    }
}
