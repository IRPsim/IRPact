package de.unileipzig.irpact.core.agent.population;

import de.unileipzig.irpact.core.agent.AgentGroup;

/**
 * @author Daniel Abitz
 */
public interface AgentPopulation {

    void set(AgentGroup<?> group, int size);

    int get(AgentGroup<?> group);

    boolean has(AgentGroup<?> group);

    int total();
}
