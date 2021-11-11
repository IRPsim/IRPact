package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class UncertaintyCache extends NameableBase {

    protected MapSupplier supplier;
    protected Map<ConsumerAgent, Uncertainty> cache;

    public UncertaintyCache() {
        this(MapSupplier.HASH);
    }

    public UncertaintyCache(MapSupplier supplier) {
        this.supplier = supplier;
        this.cache = supplier.newMap();
    }

    public Uncertainty getUncertainty(ConsumerAgent ca) {
        if(ca == null) {
            throw new NullPointerException("agent is null");
        }

        Uncertainty uncert = cache.get(ca);
        if(uncert == null) {
            throw new IllegalArgumentException("missing uncertainty for agent: " + ca.getName());
        }
        return uncert;
    }

    public boolean hasUncertainty(ConsumerAgent ca) {
        return cache.containsKey(ca);
    }

    public void register(ConsumerAgent ca, Uncertainty uncertainty) {
        cache.put(ca, uncertainty);
    }
}
