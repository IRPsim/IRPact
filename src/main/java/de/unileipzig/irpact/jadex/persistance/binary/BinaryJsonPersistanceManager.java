package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.persistence.BasicPersistManager;

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
}
