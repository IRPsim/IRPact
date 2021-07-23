package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class UncertaintyCache extends NameableBase {

    protected MapSupplier supplier;
    protected Map<ConsumerAgent, Uncertainty> uncertaintyCache;

    public UncertaintyCache() {
        this(MapSupplier.HASH);
    }

    public UncertaintyCache(MapSupplier supplier) {
        this(supplier, null);
    }

    public UncertaintyCache(String name) {
        this(MapSupplier.HASH, name);
    }

    public UncertaintyCache(MapSupplier supplier, String name) {
        setName(name);
        this.supplier = supplier;
        this.uncertaintyCache = supplier.newMap();
    }

    public Uncertainty getUncertainty(ConsumerAgent ca) {
        return uncertaintyCache.get(ca);
    }

    public void createUncertainty(ConsumerAgent ca, UncertaintyManager manager) {
        getUncertainty0(ca, manager);
    }

    protected synchronized Uncertainty getUncertainty0(ConsumerAgent ca, UncertaintyManager manager) {
        Uncertainty uncertainty = uncertaintyCache.get(ca);
        if(uncertainty == null) {
            return syncGetUncertainty0(ca, manager);
        } else {
            return uncertainty;
        }
    }

    protected synchronized Uncertainty syncGetUncertainty0(ConsumerAgent ca, UncertaintyManager manager) {
        Uncertainty uncertainty = uncertaintyCache.get(ca);
        if(uncertainty == null) {
            uncertainty = manager.createFor(ca);
            registerUncertainty(ca, uncertainty, true, false);
        }
        return uncertainty;
    }

    public void registerUncertainty(ConsumerAgent ca, Uncertainty uncertainty, boolean overwrite, boolean allowExisting) {
        if(!allowExisting && uncertaintyCache.containsKey(ca)) {
            throw new IRPactIllegalArgumentException("uncertainty for agent '{}' already exists", ca.getName());
        }

        if(!overwrite && uncertaintyCache.containsKey(ca)) {
            return;
        }

        uncertaintyCache.put(ca, uncertainty);
    }
}
