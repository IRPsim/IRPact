package de.unileipzig.irpact.core.affinity;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAffinitiesMapping<S, T> implements AffinitiesMapping<S, T> {

    protected Map<S, Affinities<T>> affinitiesMap;

    public BasicAffinitiesMapping(Map<S, Affinities<T>> affinitiesMap) {
        this.affinitiesMap = affinitiesMap;
    }

    @Override
    public boolean has(S source) {
        return affinitiesMap.containsKey(source);
    }

    @Override
    public Affinities<T> get(S source) {
        return affinitiesMap.get(source);
    }

    @Override
    public void put(S from, Affinities<T> affinities) {
        affinitiesMap.put(from, affinities);
    }

    @Override
    public void put(S from, T to, double value) {
        Affinities<T> affinities = affinitiesMap.get(from);
        affinities.setValue(to, value);
    }
}
