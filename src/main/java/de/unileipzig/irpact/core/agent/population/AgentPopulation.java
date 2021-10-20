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

    void setCoverage(double coverage);

    boolean hasCoverage();

    double getCoverage();

    void setMaximumPossibleSize(int size);

    boolean hasMaximumPossibleSize();

    int getMaximumPossibleSize();

    boolean hasScale();

    double getScale();
}
