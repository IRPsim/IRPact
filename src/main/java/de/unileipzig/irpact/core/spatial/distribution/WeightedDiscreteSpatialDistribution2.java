package de.unileipzig.irpact.core.spatial.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.develop.XXXXXXXXX;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@XXXXXXXXX("TESTEN + DataCollection einbinden")
public class WeightedDiscreteSpatialDistribution2<T> {

    protected NavigableMapWeightedMapping<T> weightedMapping = new NavigableMapWeightedMapping<>();
    protected Map<T, Collection<SpatialInformation>> unused = new LinkedHashMap<>();
    protected Map<T, Collection<SpatialInformation>> used = new LinkedHashMap<>();
    protected Rnd rnd;
    protected int numberOfCalls = 0;

    public WeightedDiscreteSpatialDistribution2() {
    }

    public void put(T key, Collection<SpatialInformation> informations) {
        unused.put(key, informations);
        weightedMapping.set(key, informations.size());
    }

    public void drawUntil(int calls) {
        while(numberOfCalls < calls) {
            drawValue();
        }
    }

    protected void checkNotEmpty() {
        if(weightedMapping.isEmpty()) {
            throw new IllegalStateException("empty");
        }
        if(unused.isEmpty()) {
            throw new IllegalStateException("empty");
        }
        int restItems = unused.values()
                .stream()
                .mapToInt(Collection::size)
                .sum();
        if(restItems == 0) {
            throw new IllegalStateException("empty");
        }
    }

    protected void removeAndUpdate(T key) {
        unused.remove(key);
        weightedMapping.remove(key);
    }

    protected SpatialInformation drawValue0(T key) {
        Collection<SpatialInformation> unusedInfos = unused.get(key);
        SpatialInformation info = rnd.removeRandom(unusedInfos);
        used.computeIfAbsent(key, _key -> new ArrayList<>()).add(info);
        if(unusedInfos.isEmpty()) {
            removeAndUpdate(key);
        }
        numberOfCalls++;
        return info;
    }

    public SpatialInformation drawValue(T key) {
        checkNotEmpty();
        return drawValue0(key);
    }

    public SpatialInformation drawValue() {
        checkNotEmpty();
        T key = weightedMapping.getWeightedRandom(rnd);
        return drawValue0(key);
    }
}
