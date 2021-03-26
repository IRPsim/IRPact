package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.simulation.BasicBinaryTaskManager;
import de.unileipzig.irpact.core.simulation.BasicInitializationData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
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
    protected BinaryJsonData doInitalizePersist(BasicJadexSimulationEnvironment object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        //===
        AgentManager am = object.getAgents();
        manager.prepareAll(am.getConsumerAgentGroups());
        manager.prepare(am.getConsumerAgentGroupAffinityMapping());
        //===
        SocialNetwork sn = object.getNetwork();
        manager.prepare(sn.getGraph());
        manager.prepare(sn.getGraphTopologyScheme());
        //===
        ProcessModelManager pmm = object.getProcessModels();
        manager.prepareAll(pmm.getProcessModels());
        //===
        ProductManager pm = object.getProducts();
        manager.prepareAll(pm.getGroups());
        //===
        manager.prepare(object.getSpatialModel());
        //===
        manager.prepare(object.getTimeModel());
        //===
        manager.prepare(object.getLiveCycleControl());
        //===
        manager.prepare(object.getVersion());
        //===
        manager.prepare(object.getSimulationRandom());
        //===

        return data;
    }

    @Override
    protected void doSetupPersist(BasicJadexSimulationEnvironment object, BinaryJsonData data, PersistManager manager) {
        AgentManager am = object.getAgents();
        data.putLongArray(manager.ensureGetAllUIDs(am.getConsumerAgentGroups()));
        data.putLong(manager.ensureGetUID(am.getConsumerAgentGroupAffinityMapping()));
        //===
        SocialNetwork sn = object.getNetwork();
        data.putLong(manager.ensureGetUID(sn.getGraph()));
        data.putLong(manager.ensureGetUID(sn.getGraphTopologyScheme()));
        //===
        ProcessModelManager pmm = object.getProcessModels();
        data.putLongArray(manager.ensureGetAllUIDs(pmm.getProcessModels()));
        //===
        ProductManager pm = object.getProducts();
        data.putLongArray(manager.ensureGetAllUIDs(pm.getGroups()));
        //===
        data.putLong(manager.ensureGetUID(object.getSpatialModel()));
        //===
        data.putLong(manager.ensureGetUID(object.getTimeModel()));
        //===
        data.putLong(manager.ensureGetUID(object.getLiveCycleControl()));
        //===
        data.putLong(manager.ensureGetUID(object.getVersion()));
        //===
        data.putLong(manager.ensureGetUID(object.getSimulationRandom()));
        //===
    }

    //=========================
    //restore
    //=========================

    @Override
    protected BasicJadexSimulationEnvironment doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        BasicJadexSimulationEnvironment environment = new BasicJadexSimulationEnvironment();
        environment.setName("Restored_Environment");
        environment.initDefault();
        return environment;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) throws RestoreException {
        setupAgentManager(data, object, manager);
        setupSocialNetwork(data, object, manager);
        setupProcessModels(data, object, manager);
        setupProductManager(data, object, manager);
        setupBinaryTaskManager(data, object, manager);
        //===
        object.setSpatialModel(manager.ensureGet(data.getLong()));
        object.setTimeModel(manager.ensureGet(data.getLong()));
        object.setLifeCycleControl(manager.ensureGet(data.getLong()));
        manager.ensureGet(data.getLong()); //Versioncheck !
        object.setSimulationRandom(manager.ensureGet(data.getLong()));
    }

    private void setupAgentManager(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        BasicAgentManager agentManager = (BasicAgentManager) object.getAgents();
        ConsumerAgentGroup[] cags = manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroup[]::new);
        for(ConsumerAgentGroup cag: cags) {
            agentManager.addConsumerAgentGroup(cag);
        }
        agentManager.setConsumerAgentGroupAffinityMapping(manager.ensureGet(data.getLong()));
    }

    private void setupSocialNetwork(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        BasicSocialNetwork socialNetwork = (BasicSocialNetwork) object.getNetwork();
        socialNetwork.setGraph(manager.ensureGet(data.getLong()));
        socialNetwork.setGraphTopologyScheme(manager.ensureGet(data.getLong()));
    }

    private void setupProcessModels(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        BasicProcessModelManager processModelManager = (BasicProcessModelManager) object.getProcessModels();
        ProcessModel[] processModels = manager.ensureGetAll(data.getLongArray(), ProcessModel[]::new);
        for(ProcessModel processModel: processModels) {
            processModelManager.addProcessModel(processModel);
        }
    }

    private void setupProductManager(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        BasicProductManager productManager = (BasicProductManager) object.getProducts();
        ProductGroup[] productGroups = manager.ensureGetAll(data.getLongArray(), ProductGroup[]::new);
        for(ProductGroup productGroup: productGroups) {
            productManager.add(productGroup);
        }
    }

    @SuppressWarnings("unused")
    private void setupBinaryTaskManager(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        BasicJadexSimulationEnvironment initialEnv = manager.getInitialInstance();

        BasicBinaryTaskManager initial = (BasicBinaryTaskManager) initialEnv.getTaskManager();
        BasicBinaryTaskManager restored = (BasicBinaryTaskManager) object.getTaskManager();

        restored.copyFrom(initial);
    }

    @Override
    protected void checkHash(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        int storedHash = data.getInt();
        int restoredHash = ((ChecksumComparable) object).getChecksum();
        if(!checkHash(object, storedHash, restoredHash)) {
            onHashMismatch(data, object, manager);
        }

        manager.setValidationHash(storedHash);
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, BasicJadexSimulationEnvironment object, RestoreManager manager) throws RestoreException {
        BasicJadexSimulationEnvironment initial = manager.getInitialInstance();
        setupInitializationData(initial, object);

        manager.setRestoredInstance(object);
    }

    private void setupInitializationData(BasicJadexSimulationEnvironment initial, BasicJadexSimulationEnvironment restored) throws RestoreException {
        BasicInitializationData initialData = (BasicInitializationData) initial.getInitializationData();
        BasicInitializationData restoredData = (BasicInitializationData) restored.getInitializationData();

        restoredData.copyFrom(initialData);

        //cags manuell, da andere instanz (initialCag.syshash != restoredCag,syshash)
        for(ConsumerAgentGroup initialCag: initial.getAgents().getConsumerAgentGroups()) {
            ConsumerAgentGroup restoredCag = restored.getAgents().getConsumerAgentGroup(initialCag.getName());
            if(restoredCag == null) {
                throw new RestoreException("restored cag '" + initialCag.getName() + "' not found");
            }
            int initialCount = initialData.getInitialNumberOfConsumerAgents(initialCag);
            restoredData.setInitialNumberOfConsumerAgents(restoredCag, initialCount);
        }
    }
}
