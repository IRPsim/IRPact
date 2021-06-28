package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.simulation.Version;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationEnvironmentPR extends BinaryPRBase<BasicJadexSimulationEnvironment> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicJadexSimulationEnvironmentPR.class);

    public static final BasicJadexSimulationEnvironmentPR INSTANCE = new BasicJadexSimulationEnvironmentPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<BasicJadexSimulationEnvironment> getType() {
        return BasicJadexSimulationEnvironment.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(BasicJadexSimulationEnvironment object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);

        manager.prepare(object.getVersion());
        manager.prepare(object.getSettings());
        manager.prepare(object.getAgents());
        manager.prepare(object.getNetwork());
        manager.prepare(object.getProcessModels());
        manager.prepare(object.getProducts());
        manager.prepare(object.getSpatialModel());
        manager.prepare(object.getTimeModel());
        manager.prepare(object.getLiveCycleControl());
        manager.prepare(object.getSimulationRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicJadexSimulationEnvironment object, BinaryJsonData data, PersistManager manager) throws PersistException {
        //Version zu erst, um beim Restore sofort den Version-Check zu haben
        data.putLong(manager.ensureGetUID(object.getVersion()));
        data.putLong(manager.ensureGetUID(object.getSettings()));
        data.putLong(manager.ensureGetUID(object.getAgents()));
        data.putLong(manager.ensureGetUID(object.getNetwork()));
        data.putLong(manager.ensureGetUID(object.getProcessModels()));
        data.putLong(manager.ensureGetUID(object.getProducts()));
        data.putLong(manager.ensureGetUID(object.getSpatialModel()));
        data.putLong(manager.ensureGetUID(object.getTimeModel()));
        data.putLong(manager.ensureGetUID(object.getLiveCycleControl()));
        data.putLong(manager.ensureGetUID(object.getSimulationRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicJadexSimulationEnvironment doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicJadexSimulationEnvironment environment = new BasicJadexSimulationEnvironment();
        manager.setRestoredRootInstance(environment);
        getRestoreHelper().getUpdater().setEnvironment(environment);

        environment.setName("Restored_Environment");
        environment.initDefault();
        environment.setRestored();

        return environment;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) throws RestoreException {
        validateVersion(data, manager);
        object.setSettings(manager.ensureGet(data.getLong()));
        object.setAgentManager(manager.ensureGet(data.getLong()));
        object.setSocialNetwork(manager.ensureGet(data.getLong()));
        object.setProcessModels(manager.ensureGet(data.getLong()));
        object.setProductManager(manager.ensureGet(data.getLong()));
        object.setSpatialModel(manager.ensureGet(data.getLong()));
        object.setTimeModel(manager.ensureGet(data.getLong()));
        object.setLifeCycleControl(manager.ensureGet(data.getLong()));
        object.setSimulationRandom(manager.ensureGet(data.getLong()));
    }

    private void validateVersion(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        Version version = manager.ensureGet(data.getLong());
        if(!IRPact.VERSION.supportsInput(version)) {
            throw ExceptionUtil.create(RestoreException::new, "version mismatch: IRPact version '{}' != '{}'", IRPact.VERSION.print(), version.print());
        }
    }

    @Override
    protected void checkChecksum(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        int storedHash = data.getInt();
        int restoredHash = ((ChecksumComparable) object).getChecksum();
        if(!checkChecksum(object, storedHash, restoredHash)) {
            onChecksumMismatch(data, object, manager);
        }
        manager.setValidationChecksum(storedHash);
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
    }
}
