package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicEdgePR extends BinaryPRBase<BasicSocialGraph.BasicEdge> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicEdgePR.class);

    public static final BasicEdgePR INSTANCE = new BasicEdgePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicSocialGraph.BasicEdge> getType() {
        return BasicSocialGraph.BasicEdge.class;
    }

    @Override
    public Persistable initalizePersist(BasicSocialGraph.BasicEdge object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putLong(manager.ensureGetUID(object.getSource().getAgent()));  //Agent!
        data.putLong(manager.ensureGetUID(object.getTarget().getAgent()));  //Agent
        data.putInt(object.getType().id());
        data.putDouble(object.getWeight());
        storeHash(object, data);
        return data;
    }

    @Override
    public BasicSocialGraph.BasicEdge initalizeRestore(Persistable persistable, RestoreManager manager) {
        return new BasicSocialGraph.BasicEdge();
    }

    @Override
    public void setupRestore(Persistable persistable, BasicSocialGraph.BasicEdge object, RestoreManager manager) {
        BasicSocialGraph graph = manager.ensureGetSameClass(BasicSocialGraph.class);
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        Agent srcAgent = manager.ensureGet(data.getLong());
        SocialGraph.Node srcNode;
        if(graph.hasNode(srcAgent)) {
            srcNode = graph.getNode(srcAgent);
        } else {
            srcNode = graph.addAgentAndGetNode(srcAgent);
            srcAgent.setSocialGraphNode(srcNode);
        }
        Agent tarAgent = manager.ensureGet(data.getLong());
        SocialGraph.Node tarNode;
        if(graph.hasNode(tarAgent)) {
            tarNode = graph.getNode(tarAgent);
        } else {
            tarNode = graph.addAgentAndGetNode(tarAgent);
            tarAgent.setSocialGraphNode(tarNode);
        }
        object.setSource(srcNode);
        object.setTarget(tarNode);
        object.setType(SocialGraph.Type.get(data.getInt()));
        object.setWeight(data.getDouble());

        graph.addEdgeDirect(object);
    }
}
