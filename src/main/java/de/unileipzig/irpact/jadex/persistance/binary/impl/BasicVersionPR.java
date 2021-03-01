package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.UncheckedParsingException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.start.IRPact;
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

    @Override
    public Persistable initalizePersist(BasicVersion object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.toString());
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicVersion initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        BasicVersion object = new BasicVersion();
        object.set(data.getText());

        if(IRPact.VERSION.isMismatch(object)) {
            throw new UncheckedParsingException("version mismatch! IRPact version: '" + IRPact.VERSION + "', input version: '" + object + "'");
        }
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicVersion object, RestoreManager manager) {
    }
}
