package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ProxyConsumerAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class ProxyConsumerAgentPR implements Persister<ProxyConsumerAgent>, Restorer<ProxyConsumerAgent> {

    public static final ProxyConsumerAgentPR INSTANCE = new ProxyConsumerAgentPR();

    @Override
    public Class<ProxyConsumerAgent> getType() {
        return ProxyConsumerAgent.class;
    }

    @Override
    public Persistable persist(ProxyConsumerAgent object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putLong(manager.ensureGetUID(object.getSpatialInformation()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        data.putDouble(object.getInformationAuthority());
        data.putLong(manager.ensureGetUID(object.getProductInterest()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAdoptedProducts()));
        data.putLong(manager.ensureGetUID(object.getProductFindingScheme()));
        data.putLong(manager.ensureGetUID(object.getProcessFindingScheme()));

        data.putLongArray(manager.ensureGetAllUIDs(object.getNeeds()));
        data.putLongLongMap(manager.ensureGetAllUIDs(object.getPlans()));
        return data;
    }

    @Override
    public ProxyConsumerAgent initalize(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        ProxyConsumerAgent object = new ProxyConsumerAgent();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setup(Persistable persistable, ProxyConsumerAgent object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEnvironment(manager.ensureGetSameClass(BasicJadexSimulationEnvironment.class));
        //...
        object.setGroup(manager.ensureGet(data.getLong()));
        object.setSpatialInformation(manager.ensureGet(data.getLong()));
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), ConsumerAgentAttribute[]::new));
        object.setInformationAuthority(data.getDouble());
        //...
        object.setProductAwareness(manager.ensureGet(data.getLong()));
        object.addAllAdoptedProducts(manager.ensureGetAll(data.getLongArray(), AdoptedProduct[]::new));
        object.setProductFindingScheme(manager.ensureGet(data.getLong()));
        object.setProcessFindingScheme(manager.ensureGet(data.getLong()));

        object.addAllNeeds(manager.ensureGetAll(data.getLongArray(), Need[]::new));
        object.addAllPlans(manager.ensureGetAll(data.getLongLongMap()));
    }

    @Override
    public void finalize(Persistable persistable, ProxyConsumerAgent object, RestoreManager manager) {
        //grp
        object.getGroup().addAgent(object);
        //node
        BasicJadexSimulationEnvironment environment = manager.ensureGetSameClass(BasicJadexSimulationEnvironment.class);
        BasicSocialNetwork socialNetwork = (BasicSocialNetwork) environment.getNetwork();
        BasicSocialGraph graph = (BasicSocialGraph) socialNetwork.getGraph();
        if(graph.hasNode(object)) {
            SocialGraph.Node node = graph.getNode(object);
            object.setSocialGraphNode(node);
        } else {
            SocialGraph.Node node = graph.addAgentAndGetNode(object);
            object.setSocialGraphNode(node);
        }
    }
}
