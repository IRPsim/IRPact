package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.*;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class WeightedDiscreteSpatialDistribution extends NameableBase implements SpatialDistribution {

    protected static final String X = "x";

    protected int requiredNumberOfCalls = 0;
    protected int nummberOfCalls = 0;

    protected BasicWeightedMapping<String, String, Number> weightedMapping;
    protected UnmodifiableWeightedMapping<String, String, Number> unmodWeightedMapping;
    protected Map<String, Collection<SpatialInformation>> unused;
    protected Map<String, Collection<SpatialInformation>> used;
    protected Rnd rnd;

    public WeightedDiscreteSpatialDistribution() {
        this(new BasicWeightedMapping<>(), new HashMap<>(), new HashMap<>());
    }

    public WeightedDiscreteSpatialDistribution(
            BasicWeightedMapping<String, String, Number> weightedMapping,
            Map<String, Collection<SpatialInformation>> unused,
            Map<String, Collection<SpatialInformation>> used) {
        this.weightedMapping = weightedMapping;
        this.unused = unused;
        this.used = used;
    }

    public void clear() {
        weightedMapping.clear();
        unmodWeightedMapping = null;
        unused.clear();
        used.clear();
    }

    public void add(String key, Collection<SpatialInformation> informations) {
        unused.put(key, informations);
    }

    public void initalize() {
        nummberOfCalls = 0;
        for(Map.Entry<String, Collection<SpatialInformation>> entry: unused.entrySet()) {
            weightedMapping.put(X, entry.getKey(), entry.getValue().size());
        }
        unmodWeightedMapping = new UnmodifiableWeightedMapping<>(weightedMapping);
        unmodWeightedMapping.makeImmutable();
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

    public int getNummberOfCalls() {
        return nummberOfCalls;
    }

    public void setRequiredNumberOfCalls(int requiredNumberOfCalls) {
        this.requiredNumberOfCalls = requiredNumberOfCalls;
    }

    public void call() {
        call(requiredNumberOfCalls);
        requiredNumberOfCalls = 0;
    }

    public void call(int times) {
        for(int i = 0; i < times; i++) {
            drawValue();
        }
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
        SpatialInformation info = CollectionUtil.removeRandom(unusedInfos, rnd.getRandom());
        Collection<SpatialInformation> usedInfos = used.computeIfAbsent(key, _key -> new ArrayList<>());
        usedInfos.add(info);
        if(unusedInfos.isEmpty()) {
            removeAndReweight(key);
        }

        nummberOfCalls++;
        return info;
    }

    @Override
    public SpatialInformation drawValue() {
        if(unused.isEmpty()) {
            throw new IllegalStateException("empty");
        }

        String key = unmodWeightedMapping.getWeightedRandom(X, rnd.getRandom());
        return drawValue(key);
    }
}
