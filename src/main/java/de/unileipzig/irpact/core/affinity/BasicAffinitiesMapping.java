package de.unileipzig.irpact.core.affinity;

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
}
