package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicVersionPR extends BinaryPRBase<BasicVersion> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicVersionPR.class);

    public static final BasicVersionPR INSTANCE = new BasicVersionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicVersion> getType() {
        return BasicVersion.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicVersion object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.toString());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicVersion doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicVersion object = new BasicVersion();
        object.set(data.getText());

        return object;
    }
}
