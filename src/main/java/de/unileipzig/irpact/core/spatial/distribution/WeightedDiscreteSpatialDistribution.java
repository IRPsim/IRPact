package de.unileipzig.irpact.core.spatial.distribution;

import de.unileipzig.irpact.commons.*;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.BasicWeightedMapping;
import de.unileipzig.irpact.commons.util.weighted.UnmodifiableWeightedMapping;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class WeightedDiscreteSpatialDistribution extends ResettableSpatialDistributionBase {

    protected static final String X = "x";

    protected BasicWeightedMapping<String, String, Number> weightedMapping;
    protected UnmodifiableWeightedMapping<String, String, Number> unmodWeightedMapping;
    protected Map<String, Collection<SpatialInformation>> unused;
    protected Map<String, Collection<SpatialInformation>> used;
    protected Rnd rnd;

    public WeightedDiscreteSpatialDistribution() {
        this(new BasicWeightedMapping<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public WeightedDiscreteSpatialDistribution(
            BasicWeightedMapping<String, String, Number> weightedMapping,
            Map<String, Collection<SpatialInformation>> unused,
            Map<String, Collection<SpatialInformation>> used) {
        this.weightedMapping = weightedMapping;
        this.unused = unused;
        this.used = used;
    }

    @Override
    public void reset() {
        numberOfCalls = 0;
        weightedMapping.clear();
        unmodWeightedMapping = null;
        unused.clear();
        used.clear();
    }

    @Override
    public boolean isShareble(SpatialDistribution target) {
        return target instanceof WeightedDiscreteSpatialDistribution;
    }

    @Override
    public void addComplexDataTo(SpatialDistribution target) {
        if(target instanceof WeightedDiscreteSpatialDistribution) {
            WeightedDiscreteSpatialDistribution targetDist = (WeightedDiscreteSpatialDistribution) target;
            targetDist.getUnused().putAll(getUnused());
            targetDist.getUsed().putAll(getUsed());
        } else {
            throw new IllegalArgumentException("requires " + getClass().getName());
        }
    }

    public void add(String key, Collection<SpatialInformation> informations) {
        unused.put(key, informations);
    }

    @Override
    public void initalize() {
        numberOfCalls = 0;
        for(Map.Entry<String, Collection<SpatialInformation>> entry: unused.entrySet()) {
            weightedMapping.put(X, entry.getKey(), entry.getValue().size());
        }
        unmodWeightedMapping = new UnmodifiableWeightedMapping<>(weightedMapping);
        unmodWeightedMapping.makeImmutable();
        call();
    }

    protected void removeAndReweight(String key) {
        unused.remove(key);
        weightedMapping.remove(X, key);
        unmodWeightedMapping = new UnmodifiableWeightedMapping<>(weightedMapping);
        unmodWeightedMapping.makeImmutable();
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRandom() {
        return rnd;
    }

    public BasicWeightedMapping<String, String, Number> getWeightedMapping() {
        return weightedMapping;
    }

    public UnmodifiableWeightedMapping<String, String, Number> getUnmodifiableWeightedMapping() {
        return unmodWeightedMapping;
    }

    public Map<String, Collection<SpatialInformation>> getUsed() {
        return used;
    }

    public Map<String, Collection<SpatialInformation>> getUnused() {
        return unused;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setRequiredNumberOfCalls(int requiredNumberOfCalls) {
        this.requiredNumberOfCalls = requiredNumberOfCalls;
    }

    public int countUnusedEntries() {
        return unused.values()
                .stream()
                .mapToInt(Collection::size)
                .sum();
    }

    public int countUsedEntries() {
        return used.values()
                .stream()
                .mapToInt(Collection::size)
                .sum();
    }

    public SpatialInformation drawValue(String key) {
        if(unused.isEmpty()) {
            throw new IllegalStateException("empty");
        }

        Collection<SpatialInformation> unusedInfos = unused.get(key);
        if(unusedInfos == null || unusedInfos.isEmpty()) {
            throw new IllegalStateException("set '" + key + "' is empty");
        }
        SpatialInformation info = rnd.removeRandom(unusedInfos);
        Collection<SpatialInformation> usedInfos = used.computeIfAbsent(key, _key -> new ArrayList<>());
        usedInfos.add(info);
        if(unusedInfos.isEmpty()) {
            removeAndReweight(key);
        }

        numberOfCalls++;
        return info;
    }

    @Override
    public SpatialInformation drawValue() {
        if(unused.isEmpty()) {
            throw new IllegalStateException("empty");
        }

        String key = unmodWeightedMapping.getWeightedRandom(X, rnd);
        return drawValue(key);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getRandom().getChecksum(),
                ChecksumComparable.getCollCollChecksum(unused.values()),
                ChecksumComparable.getCollCollChecksum(used.values())
        );
    }
}
