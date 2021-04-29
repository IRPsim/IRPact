package de.unileipzig.irpact.commons.util.data.weighted;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class RemoveOnDrawMapBasedWeightedMapping<T> extends MapBasedWeightedMapping<T> {

    public RemoveOnDrawMapBasedWeightedMapping() {
        super();
    }

    public RemoveOnDrawMapBasedWeightedMapping(Supplier<? extends Map<T, Double>> mapSupplier) {
        super(mapSupplier);
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
