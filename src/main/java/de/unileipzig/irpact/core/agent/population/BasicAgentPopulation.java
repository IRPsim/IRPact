package de.unileipzig.irpact.core.agent.population;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.AgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAgentPopulation implements AgentPopulation, ChecksumComparable {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicAgentPopulation.class);

    protected Map<AgentGroup<?>, Integer> population;
    protected int maximumPossibleSize = -1;
    protected double coverage = Double.NaN;

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
    public void setCoverage(double coverage) {
        this.coverage = coverage;
        logScale();
    }

    @Override
    public boolean hasCoverage() {
        return !Double.isNaN(coverage);
    }

    @Override
    public double getCoverage() {
        return coverage;
    }

    @Override
    public void setMaximumPossibleSize(int size) {
        this.maximumPossibleSize = size;
        logScale();
    }

    @Override
    public boolean hasMaximumPossibleSize() {
        return maximumPossibleSize != -1;
    }

    @Override
    public int getMaximumPossibleSize() {
        return maximumPossibleSize;
    }

    protected void logScale() {
        if(hasScale()) {
            calcScale(true);
        }
    }

    protected double calcScale(boolean log) {
        double scale = 1.0;

        if(hasMaximumPossibleSize()) {
            int total = total();
            if(total != maximumPossibleSize) {
                scale *= (double) total / (double) maximumPossibleSize;
                if(log) {
                    LOGGER.trace("updated scale factor (population): {}", scale);
                }
            }
        }

        if(hasCoverage()) {
            scale *= getCoverage();
            if(log) {
                LOGGER.trace("updated scale factor (coverage): {}", scale);
            }
        }

        if(log) {
            LOGGER.trace("scale factor: {}", scale);
        }
        return scale;
    }

    @Override
    public boolean hasScale() {
        return hasMaximumPossibleSize() || hasCoverage();
    }

    @Override
    public double getScale() {
        if(hasScale()) {
            return calcScale(false);
        } else {
            return Double.NaN;
        }
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getMapChecksum(population, Nameable::getName, i -> i);
    }
}
