package de.unileipzig.irpact.jadex.persistance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.PersistenceModul;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.JadexRestoreUpdater;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonRestoreManager;
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
    public void store(MetaData metaData, SimulationEnvironment environment, OutRoot root) throws Exception {
        switch (getModus()) {
            case BINARY:
                storeBinary(metaData, environment, root);
                break;

            case PARAMETER:
                throw new UnsupportedOperationException();

            default:
                throw new IllegalArgumentException("unsupported modus: " + getModus());
        }
    }

    @Override
    public SimulationEnvironment restore(
            MetaData metaData,
            MainCommandLineOptions options,
            int year,
            JadexRestoreUpdater updater,
            InRoot root) throws Exception {
        switch (getModus()) {
            case BINARY:
                return restoreBinary(metaData, options, year, updater, root);

            case PARAMETER:
                throw new UnsupportedOperationException();

            default:
                throw new IllegalArgumentException("unsupported modus: " + getModus());
        }
    }


    public ObjectNode decode(BinaryPersistData... data) throws RestoreException {
        return decode(Arrays.asList(data));
    }

    public ObjectNode decode(Collection<? extends BinaryPersistData> coll) throws RestoreException {
        switch (getModus()) {
            case BINARY:
                return decodeBinary(coll);

            case PARAMETER:
                throw new UnsupportedOperationException();

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
            MetaData metaData,
            SimulationEnvironment environment,
            OutRoot root) throws PersistException {
        binaryPersist.persist(metaData, environment);
        Collection<Persistable> persistables = binaryPersist.getPersistables();
        Set<BinaryPersistData> sortedDataList = new TreeSet<>(BinaryPersistData.ASCENDING);
        for(Persistable persistable: persistables) {
            JadexPersistable jadexPersistable = (JadexPersistable) persistable;
            BinaryPersistData data = jadexPersistable.toPersistData();
            if(!sortedDataList.add(data)) {
                throw new PersistException("uid " + data.getID() + "already exists");
            }
        }
        root.addBinaryPersistData(sortedDataList);

        LOGGER.trace("stored checksum: {}", environment.getChecksum());
    }

    public SimulationEnvironment restoreBinary(
            MetaData metaData,
            MainCommandLineOptions options,
            int year,
            JadexRestoreUpdater updater,
            InRoot root) throws IOException, RestoreException {
        if(!root.hasBinaryPersistData()) {
            throw new RestoreException("nothing to restore");
        }
        binaryRestore.unregisterAll();

        binaryRestore.setCommandLineOptions(options);
        binaryRestore.setInRoot(root);
        binaryRestore.setYear(year);
        binaryRestore.setUpdater(updater);

        List<BinaryJsonData> dataList = new ArrayList<>();
        for(BinaryPersistData bpd: root.binaryPersistData) {
            BinaryJsonData data = binaryRestore.tryRestore(bpd);
            if(data == null) {
                continue;
            }
            data.setGetMode();
            dataList.add(data);
        }

        binaryRestore.register(dataList);
        binaryRestore.restore();
        binaryRestore.restore(metaData);
        SimulationEnvironment restoredEnvironment = binaryRestore.getRestoredInstance();
        int restoredChecksum = restoredEnvironment.getChecksum();
        int validationChecksum = binaryRestore.getValidationChecksum();
        binaryRestore.unregisterAll();

        if(restoredChecksum == validationChecksum) {
            LOGGER.info("environment successfully restored");
        } else {
            String msg = "checksum mismatch: restored=" + Integer.toHexString(restoredChecksum) + " != validation=" + Integer.toHexString(validationChecksum);
            if(options.isSkipPersistenceCheck()) {
                LOGGER.warn("ignore persistence check: {}", msg);
            } else {
                throw new RestoreException(msg);
            }
        }
        return restoredEnvironment;
    }

    public ObjectNode decodeBinary(Collection<? extends BinaryPersistData> coll) throws RestoreException {
        TreeMap<Long, JsonNode> sortedNodes = new TreeMap<>();
        for(BinaryPersistData bpd: coll) {
            JsonNode restoreJson = binaryRestore.restoreJson(bpd);
            long uid = bpd.getID();
            if(sortedNodes.containsKey(uid)) {
                throw new RestoreException("uid '" + uid + "' already exists");
            }
            sortedNodes.put(uid, restoreJson);
        }
        ObjectNode out = JsonUtil.JSON.createObjectNode();
        for(Map.Entry<Long, JsonNode> entry: sortedNodes.entrySet()) {
            out.set(Long.toString(entry.getKey()), entry.getValue());
        }
        return out;
    }

    //=========================
    //param
    //=========================
}
