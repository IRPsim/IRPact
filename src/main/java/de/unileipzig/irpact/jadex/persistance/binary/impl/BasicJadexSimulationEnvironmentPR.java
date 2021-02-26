package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class BasicJadexSimulationEnvironmentPR implements Persister<BasicJadexSimulationEnvironment>, Restorer<BasicJadexSimulationEnvironment> {

    public static final BasicJadexSimulationEnvironmentPR INSTANCE = new BasicJadexSimulationEnvironmentPR();

    @Override
    public Class<BasicJadexSimulationEnvironment> getType() {
        return BasicJadexSimulationEnvironment.class;
    }

    @Override
    public Persistable persist(BasicJadexSimulationEnvironment object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        //===
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
        data.putInt(object.getHashCode());
        return data;
    }

    @Override
    public BasicJadexSimulationEnvironment initalize(Persistable persistable, RestoreManager manager) {
        BasicJadexSimulationEnvironment environment = new BasicJadexSimulationEnvironment();
        environment.initDefault();
        return environment;
    }

    @Override
    public void setup(Persistable persistable, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        //===
        BasicAgentManager agentManager = (BasicAgentManager) object.getAgents();
        ConsumerAgentGroup[] cags = manager.ensureGetAll(data.getLongArray(), ConsumerAgentGroup[]::new);
        for(ConsumerAgentGroup cag: cags) {
            agentManager.addConsumerAgentGroup(cag);
        }
        agentManager.setConsumerAgentGroupAffinityMapping(manager.ensureGet(data.getLong()));
        //===
        BasicSocialNetwork socialNetwork = (BasicSocialNetwork) object.getNetwork();
        socialNetwork.setGraph(manager.ensureGet(data.getLong()));
        socialNetwork.setGraphTopologyScheme(manager.ensureGet(data.getLong()));
        //===
        BasicProcessModelManager processModelManager = (BasicProcessModelManager) object.getProcessModels();
        ProcessModel[] processModels = manager.ensureGetAll(data.getLongArray(), ProcessModel[]::new);
        for(ProcessModel processModel: processModels) {
            processModelManager.addProcessModel(processModel);
        }
        //===
        BasicProductManager productManager = (BasicProductManager) object.getProducts();
        ProductGroup[] productGroups = manager.ensureGetAll(data.getLongArray(), ProductGroup[]::new);
        for(ProductGroup productGroup: productGroups) {
            productManager.add(productGroup);
        }
        //===
        object.setSpatialModel(manager.ensureGet(data.getLong()));
        //===
        object.setTimeModel(manager.ensureGet(data.getLong()));
        //===
        object.setLifeCycleControl(manager.ensureGet(data.getLong()));
        //===
        manager.ensureGet(data.getLong()); //Versioncheck !
        //===
        object.setSimulationRandom(manager.ensureGet(data.getLong()));
        //===
        int hash = data.getInt();
        manager.setValidationHash(hash);
    }

    @Override
    public void finalize(Persistable persistable, BasicJadexSimulationEnvironment object, RestoreManager manager) {
        manager.setRestoredInstance(object);
    }
}
