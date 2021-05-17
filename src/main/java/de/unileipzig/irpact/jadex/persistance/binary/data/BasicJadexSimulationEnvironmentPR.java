package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.Version;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.develop.XXXXXXXXX;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
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
        //===
        setupBinaryTaskManager(data, object, manager);
    }

    private void validateVersion(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        Version version = manager.ensureGet(data.getLong());
        if(!IRPact.VERSION.supportsInput(version)) {
            throw ExceptionUtil.create(RestoreException::new, "version mismatch: IRPact version '{}' != '{}'", IRPact.VERSION.print(), version.print());
        }
    }

    @XXXXXXXXX
    @SuppressWarnings("unused")
    private void setupBinaryTaskManager(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        if(true) throw new TodoException();
//        BasicJadexSimulationEnvironment initialEnv = manager.getInitialInstance();
//
//        BasicBinaryTaskManager initial = (BasicBinaryTaskManager) initialEnv.getTaskManager();
//        BasicBinaryTaskManager restored = (BasicBinaryTaskManager) object.getTaskManager();
//
//        restored.copyFrom(initial);
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

    @XXXXXXXXX
    @Override
    protected void doFinalizeRestore(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) throws RestoreException {
        if(true) throw new TodoException();
//        manager.setRestoredInstance(object);
//
//        replaceConsumerAgentGroupsInSettings(
//                manager.getInitialInstance(),
//                manager.getRestoredInstance()
//        );
    }

    protected void replaceConsumerAgentGroupsInSettings(
            BasicJadexSimulationEnvironment initial,
            BasicJadexSimulationEnvironment restored) throws RestoreException {

        Settings initialSettings = initial.getSettings();
        Settings restoredSettings = restored.getSettings();

        for(ConsumerAgentGroup initialCag: initial.getAgents().getConsumerAgentGroups()) {
            ConsumerAgentGroup restoredCag = restored.getAgents().getConsumerAgentGroup(initialCag.getName());
            if(restoredCag == null) {
                throw ExceptionUtil.create(RestoreException::new, "restored cag '{}' not found", initialCag.getName());
            }
            int initialCount = initialSettings.getInitialNumberOfConsumerAgents(initialCag);
            restoredSettings.setInitialNumberOfConsumerAgents(restoredCag, initialCount);
        }
    }
}
