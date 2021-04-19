package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class RemoveOnDrawWeightedMapping<T> extends BasicWeightedMapping<T> {

    public RemoveOnDrawWeightedMapping() {
        super();
    }

    public RemoveOnDrawWeightedMapping(Map<T, Double> mapping) {
        super(mapping);
    }

    @Override
    public T getRandom(Rnd rnd) {
        T drawn = super.getRandom(rnd);
        remove(drawn);
        return drawn;
    }

    @Override
    public T getWeightedRandom(Rnd rnd) {
        T drawn = super.getWeightedRandom(rnd);
        remove(drawn);
        return drawn;
    }
}
