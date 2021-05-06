package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.persistence.BasicPersistManager;
import de.unileipzig.irpact.commons.persistence.Persister;

/**
 * @author Daniel Abitz
 */
public class BinaryJsonPersistanceManager extends BasicPersistManager {

    public BinaryJsonPersistanceManager() {
        init();
    }

    private void init() {
        BinaryJsonUtil.registerDefaults(this);
    }

    @Override
    public <T> boolean register(Persister<T> persister) {
        if(persister instanceof BinaryPersister) {
            return super.register(persister);
        } else {
            throw new IllegalArgumentException("requires: " + BinaryPersister.class.getName());
        }
    }

    public <T> boolean register(BinaryPersister<T> persister) {
        return super.register(persister);
    }

    @Override
    public <T> void ensureRegister(Persister<T> persister) {
        if(persister instanceof BinaryPersister) {
            super.ensureRegister(persister);
        } else {
            throw new IllegalArgumentException("requires: " + BinaryPersister.class.getName());
        }
    }

    public <T> void ensureRegister(BinaryPersister<T> persister) {
        super.ensureRegister(persister);
    }
}
