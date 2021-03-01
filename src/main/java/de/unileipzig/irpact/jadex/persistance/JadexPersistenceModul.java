package de.unileipzig.irpact.jadex.persistance;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.inout.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class JadexPersistenceModul extends NameableBase implements PersistenceModul {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexPersistenceModul.class);

    protected Modus modus = Modus.getDefault();

    protected SimulationEnvironment environment;
    protected Start param;

    public JadexPersistenceModul() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setModus(Modus modus) {
        this.modus = modus;
    }

    private Modus getModus() {
        if(modus == null) {
            throw new NullPointerException("missing modus");
        }
        return modus;
    }

    @Override
    public void store(SimulationEnvironment environment, OutRoot root) throws Exception {
        switch (getModus()) {
            case BINARY:
                storeBinary(environment, root);
                break;

            case PARAMETER:
                storeParameter(environment, root);
                break;

            default:
                throw new IllegalArgumentException("unsupported modus: " + getModus());
        }
    }

    @Override
    public SimulationEnvironment restore(SimulationEnvironment initialEnvironment, InRoot root) throws Exception {
        switch (getModus()) {
            case BINARY:
                return restoreBinary(initialEnvironment, root);

            case PARAMETER:
                return restoreParameter(initialEnvironment, root);

            default:
                throw new IllegalArgumentException("unsupported modus: " + getModus());
        }
    }

    //=========================
    //binary
    //=========================

    private final PersistManager binaryPersist = new BinaryJsonPersistanceManager();
    private final RestoreManager binaryRestore = new BinaryJsonRestoreManager();

    private void storeBinary(SimulationEnvironment environment, OutRoot root) throws IOException {
        binaryPersist.persist(environment);
        Collection<Persistable> persistables = binaryPersist.getPersistables();
        List<BinaryPersistData> dataList = new ArrayList<>();
        for(Persistable persistable: persistables) {
            BinaryJsonData data = (BinaryJsonData) persistable;
            long uid = data.getUID();
            byte[] bin = data.toBytes();
            BinaryPersistData hbd = new BinaryPersistData();
            hbd.setID(uid);
            hbd.setBytes(bin);
            dataList.add(hbd);
        }
        root.addHiddenBinaryData(dataList);
    }

    public SimulationEnvironment restoreBinary(SimulationEnvironment initialEnvironment, InRoot root) throws IOException, RestoreException {
        if(!root.hasBinaryPersistData()) {
            return initialEnvironment;
        }

        List<BinaryJsonData> dataList = new ArrayList<>();
        for(BinaryPersistData hdb: root.binaryPersistData) {
            BinaryJsonData data = BinaryJsonData.restore(hdb.getBytes());
            data.setGetMode();
            dataList.add(data);
        }
        binaryRestore.setInitialInstance(initialEnvironment);
        binaryRestore.restore(dataList);
        SimulationEnvironment restoredEnvironment = binaryRestore.getRestoredInstance();
        int restoredHash = restoredEnvironment.getHashCode();
        int validationHash = binaryRestore.getValidationHash();
        if(restoredHash == validationHash) {
            LOGGER.info("environment successfully restored");
        } else {
            String msg = "hash mismatch: restored=" + Integer.toHexString(restoredHash) + " != validation=" + Integer.toHexString(validationHash);
            if(environment.getInitializationData().ignorePersistenceCheckResult()) {
                LOGGER.warn("ignore persistence check: {}", msg);
            } else {
                throw new RestoreException(msg);
            }
        }
        return restoredEnvironment;
    }

    //=========================
    //param
    //=========================

    private void storeParameter(SimulationEnvironment environment, OutRoot root) {
        throw new UnsupportedOperationException();
    }

    public SimulationEnvironment restoreParameter(SimulationEnvironment initialEnvironment, InRoot root) {
        throw new UnsupportedOperationException();
    }
}
