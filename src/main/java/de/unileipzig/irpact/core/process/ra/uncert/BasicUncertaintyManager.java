package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyManager extends NameableBase implements UncertaintyManager {

    protected Set<UncertaintySupplier> suppliers;

    public BasicUncertaintyManager() {
        this(new LinkedHashSet<>());
    }

    public BasicUncertaintyManager(Set<UncertaintySupplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void setSuppliers(Set<UncertaintySupplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Set<UncertaintySupplier> getSuppliers() {
        return suppliers;
    }

    @Override
    public void initalize() {
        for(UncertaintySupplier supplier: suppliers) {
            supplier.initalize();
        }
    }

    @Override
    public boolean register(UncertaintySupplier supplier) {
        return suppliers.add(supplier);
    }

    @Override
    public boolean unregister(UncertaintySupplier supplier) {
        return suppliers.remove(supplier);
    }

    @Override
    public Uncertainty createFor(ConsumerAgent agent) {
        for(UncertaintySupplier supplier: suppliers) {
            if(supplier.isSupported(agent)) {
                return supplier.createFor(agent);
            }
        }
        return null;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getSetChecksum(suppliers);
    }
}
