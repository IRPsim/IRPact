package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.NameableBase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicAwarenessDistributionMapping<T, D> extends NameableBase implements AwarenessDistributionMapping<T, D> {

    protected Map<T, D> mapping;

    public BasicAwarenessDistributionMapping() {
        this(new HashMap<>());
    }

    public BasicAwarenessDistributionMapping(Map<T, D> mapping) {
        this.mapping = mapping;
    }

    @Override
    public void put(T item, D distribution) {
        mapping.put(item, distribution);
    }

    @Override
    public D getDistribution(T item) {
        return mapping.get(item);
    }
}
