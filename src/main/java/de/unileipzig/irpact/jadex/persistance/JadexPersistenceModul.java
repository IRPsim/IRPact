package de.unileipzig.irpact.jadex.persistance;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class JadexPersistenceModul extends NameableBase implements PersistenceModul {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexPersistenceModul.class);

    protected Modus modus;

    protected SimulationEnvironment environment;

    public JadexPersistenceModul() {
        setModus(Modus.BINARY);
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
    public SimulationEnvironment restore(
            MainCommandLineOptions options,
            InRoot root) throws Exception {
        switch (getModus()) {
            case BINARY:
                return restoreBinary(options, root);

            case PARAMETER:
                return restoreParameter(options, root);

            default:
                throw new IllegalArgumentException("unsupported modus: " + getModus());
        }
    }

    //=========================
    //binary
    //=========================

    private final BinaryJsonPersistanceManager binaryPersist = new BinaryJsonPersistanceManager();
    private final BinaryJsonRestoreManager binaryRestore = new BinaryJsonRestoreManager();

    private void storeBinary(
            SimulationEnvironment environment,
            OutRoot root) throws PersistException, IOException {
        binaryPersist.persist(environment);
        Collection<Persistable> persistables = binaryPersist.getPersistables();
        Set<BinaryPersistData> sortedDataList = new TreeSet<>(BinaryPersistData.ASCENDING);
        for(Persistable persistable: persistables) {
            BinaryJsonData data = (BinaryJsonData) persistable;
            long uid = data.getUID();
            byte[] bin = data.toBytes();
            BinaryPersistData hbd = new BinaryPersistData();
            hbd.setID(uid);
            hbd.setBytes(bin);
            if(!sortedDataList.add(hbd)) {
                throw new PersistException("uid " + uid + "already exists");
            }
        }
        root.addHiddenBinaryData(sortedDataList);

        LOGGER.trace("stored checksum: {}", environment.getChecksum());
    }

    public SimulationEnvironment restoreBinary(
            MainCommandLineOptions options,
            InRoot root) throws IOException, RestoreException {
        if(!root.hasBinaryPersistData()) {
            throw new RestoreException("nothing to restore");
        }

        binaryRestore.setCommandLineOptions(options);
        binaryRestore.setInRoot(root);

        List<BinaryJsonData> dataList = new ArrayList<>();
        for(BinaryPersistData hdb: root.binaryPersistData) {
            BinaryJsonData data = BinaryJsonData.restore(hdb.getBytes());
            data.setGetMode();
            dataList.add(data);
        }

        binaryRestore.unregisterAll();
        binaryRestore.register(dataList);
        binaryRestore.restore();
        SimulationEnvironment restoredEnvironment = binaryRestore.getRestoredInstance();
        binaryRestore.unregisterAll();

        int restoredChecksum = restoredEnvironment.getChecksum();
        int validationChecksum = binaryRestore.getValidationChecksum();
        if(restoredChecksum == validationChecksum) {
            LOGGER.info("environment successfully restored");
        } else {
            String msg = "checksum mismatch: restored=" + Integer.toHexString(restoredChecksum) + " != validation=" + Integer.toHexString(validationChecksum);
            if(environment.getSettings().ignorePersistenceCheckResult()) {
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

    private void storeParameter(
            SimulationEnvironment environment,
            OutRoot root) {
        throw new UnsupportedOperationException();
    }

    public SimulationEnvironment restoreParameter(
            MainCommandLineOptions options,
            InRoot root) {
        throw new UnsupportedOperationException();
    }
}
