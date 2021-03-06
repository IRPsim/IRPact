package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentValueAttribute;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.agents.consumer.ProxyConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ProxyConsumerAgentPR extends BinaryPRBase<ProxyConsumerAgent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProxyConsumerAgentPR.class);

    public static final ProxyConsumerAgentPR INSTANCE = new ProxyConsumerAgentPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ProxyConsumerAgent> getType() {
        return ProxyConsumerAgent.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ProxyConsumerAgent object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putInt(object.getMaxNumberOfActions());
        data.putLong(object.getActingOrder());

        manager.prepare(object.getGroup());
        manager.prepare(object.getSpatialInformation());
        manager.prepareAll(object.getAttributes());
        manager.prepareAll(object.getProductRelatedAttributes());

        manager.prepare(object.getProductAwareness());
        manager.prepare(object.getProductInterest());
        manager.prepareAll(object.getAdoptedProducts());
        manager.prepare(object.getProductFindingScheme());
        manager.prepare(object.getProcessFindingScheme());

        manager.prepareAll(object.getNeeds());
        manager.prepareAll(object.getActivePlans());

        return data;
    }

    @Override
    protected void doSetupPersist(ProxyConsumerAgent object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getGroup()));
        data.putLong(manager.ensureGetUID(object.getSpatialInformation()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAttributes()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getProductRelatedAttributes()));
        data.putDouble(object.getInformationAuthority());

        data.putLong(manager.ensureGetUID(object.getProductAwareness()));
        data.putLong(manager.ensureGetUID(object.getProductInterest()));
        data.putLongArray(manager.ensureGetAllUIDs(object.getAdoptedProducts()));
        data.putLong(manager.ensureGetUID(object.getProductFindingScheme()));
        data.putLong(manager.ensureGetUID(object.getProcessFindingScheme()));

        data.putLongArray(manager.ensureGetAllUIDs(object.getNeeds()));
        data.putLongLongMap(manager.ensureGetAllUIDs(object.getActivePlans()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ProxyConsumerAgent doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        ProxyConsumerAgent object = new ProxyConsumerAgent();
        object.setName(data.getText());
        object.setMaxNumberOfActions(data.getInt());
        object.setActingOrder(data.getLong());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProxyConsumerAgent object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));


        object.setGroup(manager.ensureGet(data.getLong()));
        object.setSpatialInformation(manager.ensureGet(data.getLong()));
        object.addAllAttributes(manager.ensureGetAll(data.getLongArray(), ConsumerAgentValueAttribute[]::new));
        object.addAllProductRelatedAttribute(manager.ensureGetAll(data.getLongArray(), ConsumerAgentProductRelatedAttribute[]::new));
        object.setInformationAuthority(data.getDouble());

        object.setProductAwareness(manager.ensureGet(data.getLong()));
        object.setProductInterest(manager.ensureGet(data.getLong()));
        object.addAllAdoptedProducts(manager.ensureGetAll(data.getLongArray(), AdoptedProduct[]::new));
        object.setProductFindingScheme(manager.ensureGet(data.getLong()));
        object.setProcessFindingScheme(manager.ensureGet(data.getLong()));

        object.addAllNeeds(manager.ensureGetAll(data.getLongArray(), Need[]::new));
        object.addAllActivePlans(manager.ensureGetAll(data.getLongLongMap()));
        if(true) throw new IllegalStateException("runningPlans missing");
    }

    @Override
    protected void doFinalizeRestore(BinaryJsonData data, ProxyConsumerAgent object, RestoreManager manager) throws RestoreException {
        //grp
        object.getGroup().addAgent(object);
        //access
        object.linkAccess(object.getSpatialInformation().getAttributeAccess());
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
        //spatial
        if(!object.getGroup().getSpatialDistribution().setUsed(object.getSpatialInformation())) {
            throw new RestoreException("[{}] failed to set spatial information '{}' as used", object.getName(), object.getSpatialInformation());
        }
    }
}
