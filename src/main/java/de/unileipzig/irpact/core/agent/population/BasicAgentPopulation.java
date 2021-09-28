package de.unileipzig.irpact.core.agent.population;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.AgentGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAgentPopulation implements AgentPopulation, ChecksumComparable {

    protected Map<AgentGroup<?>, Integer> population;
    protected int maximumPossibleSize = -1;

    public BasicAgentPopulation() {
        this(new HashMap<>());
    }

    public BasicAgentPopulation(Map<AgentGroup<?>, Integer> population) {
        this.population = population;
    }

    @Override
    public void set(AgentGroup<?> group, int size) {
        population.put(group, size);
    }

    @Override
    public int get(AgentGroup<?> group) {
        Integer size = population.get(group);
        return size == null ? 0 : size;
    }

    @Override
    public boolean has(AgentGroup<?> group) {
        return population.containsKey(group);
    }

    @Override
    public int total() {
        return population.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    @Override
    public void setMaximumPossibleSize(int size) {
        this.maximumPossibleSize = size;
    }

    @Override
    public int getMaximumPossibleSize() {
        return maximumPossibleSize;
    }

    @Override
    public boolean hasScale() {
        return maximumPossibleSize != -1;
    }

    @Override
    public double getScale() {
        if(maximumPossibleSize == -1) {
            return Double.NaN;
        }
        int total = total();
        return total == maximumPossibleSize
                ? 1.0
                : (double) total / (double) maximumPossibleSize;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getMapChecksum(population, Nameable::getName, i -> i);
    }
}
